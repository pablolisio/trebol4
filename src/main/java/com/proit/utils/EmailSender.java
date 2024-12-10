package com.proit.utils;

import java.io.Serializable;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.wicket.RuntimeConfigurationType;

public class EmailSender implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private static final String HOST = "localhost";
	private static final String PORT = "25";
	private static final String FROM = "info@trebol4sistema.com.ar"; //Cree desde Ferozo la casilla
	
	public static final int TO = 1;
	public static final int CC = 2;
	public static final int BCC = 3;
	
	public EmailSender(){
	}

	public static boolean sendEmail(String addressListCommaSeparated, String subject, String text, int type, RuntimeConfigurationType runtimeConfigurationType) throws AddressException, MessagingException {
		
		if (runtimeConfigurationType.equals(RuntimeConfigurationType.DEVELOPMENT)) {
			return true;
		}
		
		Properties props = new Properties();
		props.put("mail.smtp.host", HOST);
		props.put("mail.smtp.port", PORT);
		
		javax.mail.Session session = javax.mail.Session.getDefaultInstance(props);
		
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(FROM));
		
		//Le quito los espacios a la lista de contactos
		message.setRecipients( getRecipientType(type), InternetAddress.parse(addressListCommaSeparated.replaceAll("\\s","")) );
		message.setSubject(subject);
		message.setText(text);
		Transport.send(message);
		return true;
	}

	private static Message.RecipientType getRecipientType(int type) {
		Message.RecipientType recipientType;
		switch (type) {
			case TO:
				recipientType = Message.RecipientType.TO;
				break;
			case CC:
				recipientType = Message.RecipientType.CC;
				break;
			default:
				recipientType = Message.RecipientType.BCC;
				break;
		};
		return recipientType;
	}
}