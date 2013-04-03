package app.util.xml;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SAXXMLReaderUTF8 {

	public static void main(String[] args) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser parser = factory.newSAXParser();

			DefaultHandler handler = new DefaultHandler() {
				boolean bfname = false;
				boolean blname = false;
				boolean bnname = false;
				boolean bsalary = false;
				
				// TODO: Implement xml from moodle WS

				@Override
				public void startElement(String uri, String localName,
						String qName, Attributes attributes)
								throws SAXException {
					// TODO Auto-generated method stub
					//super.startElement(uri, localName, qName, attributes);

					System.out.println("Start Element :" + qName);

					if (qName.equalsIgnoreCase("FIRSTNAME")) {
						bfname = true;
					}

					if (qName.equalsIgnoreCase("LASTNAME")) {
						blname = true;
					}

					if (qName.equalsIgnoreCase("NICKNAME")) {
						bnname = true;
					}

					if (qName.equalsIgnoreCase("SALARY")) {
						bsalary = true;
					}
				}

				@Override
				public void endElement(String uri, String localName,
						String qName) throws SAXException {
					// TODO Auto-generated method stub
					//super.endElement(uri, localName, qName);

					System.out.println("End Element :" + qName);
				}

				@Override
				public void characters(char[] ch, int start, int length)
						throws SAXException {
					// TODO Auto-generated method stub
					//super.characters(ch, start, length);

					System.out.println(new String(ch, start, length));

					if (bfname) {
						System.out.println("First Name : " + new String(ch, start, length));
						bfname = false;
					}

					if (blname) {
						System.out.println("Last Name : " + new String(ch, start, length));
						blname = false;
					}

					if (bnname) {
						System.out.println("Nick Name : " + new String(ch, start, length));
						bnname = false;
					}

					if (bsalary) {
						System.out.println("Salary : " + new String(ch, start, length));
						bsalary = false;
					}
				}
			};

			File file = new File("");
			InputStream is = new FileInputStream(file);
			Reader reader = new InputStreamReader(is, "UTF-8");

			InputSource inputSource = new InputSource(reader);
			inputSource.setEncoding("UTF-8");

			parser.parse(is, handler);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}
