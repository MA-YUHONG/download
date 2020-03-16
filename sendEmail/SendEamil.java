package org.westos.email;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.sun.mail.util.MailSSLSocketFactory;

//发送普通文字邮件
public class SendEamil
{
	static String sendnameString = "110@qq.com";// 发送人邮箱
	static String codeString = "----";// 发送人授权码
	static String contextString = "java发送邮件成功";// 发送内容
	static String headTitleString = "java发送邮件";// 标题
	static String getNameString = "110@qq.com";// 收件人邮箱

	/**
	 * 纯文本发送
	 * 
	 * @param args
	 * @throws MessagingException
	 * @throws GeneralSecurityException
	 */
	public static void main(String[] args) throws MessagingException, GeneralSecurityException
	{
		// 创建一个配置文件并保存
		Properties properties = new Properties();

		properties.setProperty("mail.host", "smtp.qq.com");

		properties.setProperty("mail.transport.protocol", "smtp");

		properties.setProperty("mail.smtp.auth", "true");

		// QQ存在一个特性设置SSL加密
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		properties.put("mail.smtp.ssl.enable", "true");
		properties.put("mail.smtp.ssl.socketFactory", sf);

		// 创建一个session对象
		Session session = Session.getDefaultInstance(properties, new Authenticator()
		{
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				return new PasswordAuthentication(sendnameString, codeString);
			}
		});

		// 开启debug模式
		session.setDebug(true);

		// 获取连接对象
		Transport transport = session.getTransport();

		// 连接服务器
		transport.connect("smtp.qq.com", sendnameString, codeString);

		// 创建邮件对象
		MimeMessage mimeMessage = new MimeMessage(session);

		// 邮件发送人
		mimeMessage.setFrom(new InternetAddress(sendnameString));

		// 邮件接收人
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(getNameString));

		// 邮件标题
		mimeMessage.setSubject(headTitleString);

		// 邮件内容
		mimeMessage.setContent(contextString, "text/html;charset=UTF-8");

		// 发送邮件
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

		// 关闭连接
		transport.close();
		System.out.println("发送成功1");
	}
}
