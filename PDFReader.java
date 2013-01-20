package com.kse.util;

import java.io.FileInputStream;
import java.io.InputStream;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;



public class PDFReader {
  public static String convertPDFToText(String file){
		
		String text = null;
		try{
			InputStream in = new FileInputStream(file);
			PDFParser parser = new PDFParser(in);
			parser.parse();
			PDDocument pdd = parser.getPDDocument();
			COSDocument cos = parser.getDocument();
			PDFTextStripper stripper = new PDFTextStripper();
			text = stripper.getText(pdd);
			cos.close();
			pdd.close();			
		}
		catch(Exception e){
			System.out.println("ex = "+e.toString());
		}
		return text;
	}
}
