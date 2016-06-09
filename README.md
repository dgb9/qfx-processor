# qfx-processor
Given a QFX file, this library processes it and returns a list of transactions

A QFX file is the file that is generated and it is required to be
imported to Quicken. This is the most popular export format for
most of the banks in North America and probably in the rest of the world.

At least at my bank, the other options you got are QIF file which was
used ages ago for Microsoft Money, a product currently no longer supported
by its producer, and CSV.

So if you want to create your own financial software and you want to
be able to upload the QFX files that were downloaded from the financial institution,
please give this library a try.

General
The QFX file is a text file that looks like an xml, except most of the tags
have only the start tag and not the end one.

The file has a header like below, that can be ignored without any issue:

OFXHEADER:100
DATA:OFXSGML
VERSION:102
SECURITY:NONE
ENCODING:USASCII
CHARSET:1252
COMPRESSION:NONE
OLDFILEUID:NONE
NEWFILEUID:NONE


Then there are some entries where the name and some other general information about
the bank and the account are to be found as below:

Then the module looks for two entries with the following tags:

<BANKID>
<ACCTID>

Those contain the bank id and the account id. In the past they were placed in some parent
element, currently this is no longer guaranteed so we simply look for them and retrieve
the values, they only appear once per file.

And the regular transaction looks like this:

<STMTTRN>
<TRNTYPE>DEBIT
<DTPOSTED>20160524000000
<DTUSER>20160523000000
<TRNAMT>-30.45
<FITID>20161451905201D000155134426144800142974401
<SIC>5912
<NAME>WELL.CA INC
</STMTTRN>

Looking for STMTTRN tag - this is one of the few tags that have both start and end - and retrieve the info
for each transaction.

For each transaction, we were interested in the following fields:

	// the following two are once per file
	private String bankId;
	private String accountId;

	// these are once per transaction
 	private String id;
	private String name;
	private BigDecimal amount;
	private Date postedDate;
	private String transactionType;

=========== HOW TO USE IT

- Get a stream that goes towards you qfx
- Start the processor and process the stream
- Get a list of transactions

The test code is as follows:

        // the following two lines actually show the entire processing, the rest is just to
        // display the results
        InputStream stream = new FileInputStream("C:\\Daniel\\idea\\qfx-processor\\config\\report.qfx");
        List<QfxData> res = new QfxParser().parseData(stream);

        int index = 0;

        for (QfxData dt : res) {
            index ++;

            System.out.println("dt: " + index + ": " + dt);
        }

        stream.close();


So ran the program in Test.java over the report.qfx which is in the
config folder and the result is the one below. The accountId has been changed
as the data is real and I replaced most of the digits with some stars, however
in real life the entire account id is populated properly.

dt: 1: QfxData [bankId=null, accountId=5181********3140, name=WELL.CA INC, id=20161451905201D000155134426144800142974401, amount=-30.45, postedDate=Tue May 24 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 2: QfxData [bankId=null, accountId=5181********3140, name=IKEA NORTH YORK, id=20161451905191D000155134426143800138133609, amount=-37.27, postedDate=Tue May 24 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 3: QfxData [bankId=null, accountId=5181********3140, name=WWW.STEAMPOWERED.COM, id=20161451905181D000115505436143060625501016, amount=-21.99, postedDate=Tue May 24 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 4: QfxData [bankId=null, accountId=5181********3140, name=A3 AEGEAN 39022925264289, id=20161411858499D000115423996141000104310160, amount=-122.21, postedDate=Fri May 20 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 5: QfxData [bankId=null, accountId=5181********3140, name=A3 AEGEAN 39022925264279, id=20161411858489D000115423996141000104310152, amount=-122.21, postedDate=Fri May 20 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 6: QfxData [bankId=null, accountId=5181********3140, name=A3 AEGEAN 39022925264269, id=20161411858479D000115423996141000104310145, amount=-122.21, postedDate=Fri May 20 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 7: QfxData [bankId=null, accountId=5181********3140, name=KOODO MOBILE PAC, id=20161401844397D000155181366139463666888346, amount=-103.79, postedDate=Thu May 19 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 8: QfxData [bankId=null, accountId=5181********3140, name=NETFLIX.COM, id=20161371852260D000155490536137000754572923, amount=-7.99, postedDate=Mon May 16 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 9: QfxData [bankId=null, accountId=5181********3140, name=THE TORONTO SYMPHON, id=20161371852250D000155181366134549434174442, amount=-213.25, postedDate=Mon May 16 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 10: QfxData [bankId=null, accountId=5181********3140, name=HIGHLAND FARMS SCARBOR, id=20161371852240D000155181366134620667911535, amount=-186.84, postedDate=Mon May 16 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 11: QfxData [bankId=null, accountId=5181********3140, name=DR. CAROL WALDMAN, id=20161331845176D000155134426132800159360761, amount=-321.0, postedDate=Thu May 12 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 12: QfxData [bankId=null, accountId=5181********3140, name=STEAMGAMES.COM, id=20161321841560D000115276326132000263448909, amount=-19.99, postedDate=Wed May 11 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 13: QfxData [bankId=null, accountId=5181********3140, name=Amazon Digital Dwnlds, id=20161311842026D000155490536130000301899255, amount=-2.05, postedDate=Tue May 10 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 14: QfxData [bankId=null, accountId=5181********3140, name=Amazon.ca, id=20161311842016D000155490536130000180785187, amount=-33.89, postedDate=Tue May 10 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 15: QfxData [bankId=null, accountId=5181********3140, name=MR GREEK MEDITERRANEAN, id=20161301849152D000155419216129000371648301, amount=-98.98, postedDate=Mon May 09 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 16: QfxData [bankId=null, accountId=5181********3140, name=PIZZA PIZZA # 69, id=20161301849142D000175259116127920369077202, amount=-21.7, postedDate=Mon May 09 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 17: QfxData [bankId=null, accountId=5181********3140, name=GOOGLE *YouTube, id=20161301849132D000155490536129000278037502, amount=-4.99, postedDate=Mon May 09 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 18: QfxData [bankId=null, accountId=5181********3140, name=Amazon *Marketplce CA, id=20161271848423D000155490536127000223178809, amount=-4.31, postedDate=Fri May 06 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 19: QfxData [bankId=null, accountId=5181********3140, name=YAK COMMUNICATIONS  C, id=20161271848413D000135505436127050143144405, amount=-18.94, postedDate=Fri May 06 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 20: QfxData [bankId=null, accountId=5181********3140, name=INDIGO - CONSILIUM PL, id=20161251845169D000155134426124800104879592, amount=-79.3, postedDate=Wed May 04 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 21: QfxData [bankId=null, accountId=5181********3140, name=MTO RUS- SO ECHANNEL, id=20161251845159D000175259116123920228345208, amount=-108.0, postedDate=Wed May 04 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 22: QfxData [bankId=null, accountId=5181********3140, name=PAYMENT / PAIEMENT - TD, id=20161241847545C001975181276124101200867063, amount=427.0, postedDate=Tue May 03 00:00:00 EDT 2016, transactionType=CREDIT]
dt: 23: QfxData [bankId=null, accountId=5181********3140, name=MCDONALD'S #29005  QPS, id=20161241847535D000155134426123800104930859, amount=-3.65, postedDate=Tue May 03 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 24: QfxData [bankId=null, accountId=5181********3140, name=ALPHABET KIDS INC., id=20161241847525D000155134426123800176434640, amount=-72.0, postedDate=Tue May 03 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 25: QfxData [bankId=null, accountId=5181********3140, name=SECOND CUP # 9402, id=20161231853502D000155419216123000904321703, amount=-6.73, postedDate=Mon May 02 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 26: QfxData [bankId=null, accountId=5181********3140, name=LINODE.COM, id=20161231853492D000175418236122025680628193, amount=-12.87, postedDate=Mon May 02 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 27: QfxData [bankId=null, accountId=5181********3140, name=J CREW RETAIL #594, id=20161231853482D000155310206123838000261377, amount=-32.86, postedDate=Mon May 02 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 28: QfxData [bankId=null, accountId=5181********3140, name=TSI INTERNET, id=20161231853472D000155134426122800152952251, amount=-56.44, postedDate=Mon May 02 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 29: QfxData [bankId=null, accountId=5181********3140, name=JACK ASTOR'S WALDE, id=20161231853462D000125536066123104019168296, amount=-77.22, postedDate=Mon May 02 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 30: QfxData [bankId=null, accountId=5181********3140, name=FAMOUSFOOTWEAR#78, id=20161231853452D000155464966123091945000244, amount=-77.62, postedDate=Mon May 02 00:00:00 EDT 2016, transactionType=DEBIT]
dt: 31: QfxData [bankId=null, accountId=5181********3140, name=DIGITAL OCEAN CANADA, id=20161231853442D000185205086122617580847314, amount=-14.54, postedDate=Mon May 02 00:00:00 EDT 2016, transactionType=DEBIT]
