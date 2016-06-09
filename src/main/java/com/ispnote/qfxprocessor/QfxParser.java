package com.ispnote.qfxprocessor;

import com.ispnote.qfxprocessor.QfxData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QfxParser {
	private static final String ACCOUNT_ACCTID = "<ACCTID>";
	private static final String ACCOUNT_BANKID = "<BANKID>";
	
	private static final String TRANSACTION_CLOSE = "</STMTTRN>";
	private static final String TRANSACTION_NAME = "<NAME>";
	private static final String TRANSACTION_FITID = "<FITID>";
	private static final String TRANSACTION_TRNAMT = "<TRNAMT>";
	private static final String TRANSACTION_DTPOSTED = "<DTPOSTED>";
	private static final String TRANSACTION_TRNTYPE = "<TRNTYPE>";

	public QfxParser(){
		
	}
	
	public List<QfxData> parseData(InputStream stream) throws  QfxException {
		List<QfxData> res = null;

		try {
			List<String> lines = getLines(stream);

			res = parseData(lines);
		}
		catch (Exception e) {
			throw new QfxException(e);
		}

		return res;
	}

	private List<String> getLines(InputStream stream) throws IOException {
		List<String> res = new ArrayList<String>();

		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String line = null;

		do {
			line = reader.readLine();

			// add only non empty lines
			if(line != null && line.trim().length() > 0){
				res.add(line);
			}
		}
		while (line != null);

		return res;
	}

	public List<QfxData> parseData(List<String> lines) throws QfxException {
		List<QfxData> res = new ArrayList<QfxData>();
		
		String bankId = getBankId(lines); 
		String accountId = getAccountId(lines); 
		
		boolean inTransaction = false;
		QfxData data = null;
		
		try {
			for(String line : lines){
				String ln = line.trim();
				
				if(inTransaction){
					if(ln.startsWith(TRANSACTION_TRNTYPE)){
						String strType = getRemaining(ln, TRANSACTION_TRNTYPE);
						
						data.setTransactionType(strType);
					}
					else if(ln.startsWith(TRANSACTION_DTPOSTED)){
						String strDate = getRemaining(ln, TRANSACTION_DTPOSTED);
						Date dt = parseDate(strDate);
						
						data.setPostedDate(dt);
					}
					else if(ln.startsWith(TRANSACTION_TRNAMT)){
						String strAmount = getRemaining(ln, TRANSACTION_TRNAMT); 
						BigDecimal amount = new BigDecimal(strAmount);
						
						data.setAmount(amount);
					}
					else if(ln.startsWith(TRANSACTION_FITID)){
						String fitId = getRemaining(ln, TRANSACTION_FITID);
						
						data.setId(fitId);
					}
					else if(ln.startsWith(TRANSACTION_NAME)){
						String transactionName = getRemaining(ln, TRANSACTION_NAME);
						
						data.setName(transactionName);
					}
					else if(ln.startsWith(TRANSACTION_CLOSE)){
						inTransaction = false; 
						
						data = null;
					}
				}
				else {
					if(ln.startsWith("<STMTTRN>")){
						inTransaction = true; 
						data = new QfxData();
						data.setAccountId(accountId);
						data.setBankId(bankId);
						
						res.add(data);
					}
				}
			}
		}
		catch(Exception e){
			throw new QfxException(e);
		}
		
		return res ; 
	}

	private Date parseDate(String strDate) throws ParseException {
		Date dt = null;
		int index = strDate.indexOf("["); 
		
		String rs = strDate; 
		if(index >= 0){
			rs = strDate.substring(0, index);
		}
		
		if(rs.length() > 0){
			DateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
			dt = format.parse(rs);
		}
		
		return dt; 
	}

	private String getRemaining(String line, String prefix) {
		int nr = prefix.length();
		return line.substring(nr);
	}

	private String getBankId(List<String> lines) {
		return getHeaderValue(lines, ACCOUNT_BANKID);
	}

	private String getAccountId(List<String> lines) {
		return getHeaderValue(lines, ACCOUNT_ACCTID);
	}

	private String getHeaderValue(List<String> lines, String prefix) {
		String res = null;
		
		for(String line : lines){
			String ln = line.trim();
			
			// we consider all the lines - the header delimiter is not reliable
			
			if(ln.startsWith(prefix)){
				res = getRemaining(ln, prefix);
				
				break; // we found what we were looking for
			}
		}
		
		return res;
	}

}
