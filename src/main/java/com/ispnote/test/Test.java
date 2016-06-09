package com.ispnote.test;

import com.ispnote.qfxprocessor.QfxData;
import com.ispnote.qfxprocessor.QfxParser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created by dgabrove on 06/09/2016.
 */
public class Test {
    public static void main(String[] args) {
        try {
            new Test().process();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void process() throws Exception {
        InputStream stream = new FileInputStream("C:\\Daniel\\idea\\qfx-processor\\config\\report.qfx");
        List<QfxData> res = new QfxParser().parseData(stream);

        int index = 0;

        for (QfxData dt : res) {
            index ++;

            System.out.println("dt: " + index + ": " + dt);
        }

        stream.close();
    }
}
