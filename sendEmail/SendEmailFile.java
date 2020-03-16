package org.westos.email;

import java.security.GeneralSecurityException;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.sun.mail.util.MailSSLSocketFactory;

//发送邮件带文件
public class SendEmailFile
{
	static String sendnameString = "110@qq.com";// 发送人邮箱
	static String codeString = "------";// 发送人授权码
	static String contextString = "java发送邮件成功";// 发送内容
	static String headTitleString = "java发送邮件";// 标题
	static String imgUrlString = "C:\\Users\\123\\Pictures\\www.myhgh.club.png";// 背景图片地址
	static String fileString = "d:\\temp.sql";// 文件地址
	static String getNameString = "110@qq.com";// 收件人邮箱

	/**
	 * 带附件发送
	 * 
	 * @param args
	 * @throws GeneralSecurityException
	 * @throws MessagingException
	 */
	public static void main(String[] args) throws GeneralSecurityException, MessagingException
	{

		Properties prop = new Properties();
		prop.setProperty("mail.host", "smtp.qq.com"); //// 设置QQ邮件服务器
		prop.setProperty("mail.transport.protocol", "smtp"); // 邮件发送协议
		prop.setProperty("mail.smtp.auth", "true"); // 需要验证用户名密码

		// QQ邮箱设置SSL加密
		MailSSLSocketFactory sf = new MailSSLSocketFactory();
		sf.setTrustAllHosts(true);
		prop.put("mail.smtp.ssl.enable", "true");
		prop.put("mail.smtp.ssl.socketFactory", sf);

		// 1、创建定义整个应用程序所需的环境信息的 Session 对象
		Session session = Session.getDefaultInstance(prop, new Authenticator()
		{
			@Override
			protected PasswordAuthentication getPasswordAuthentication()
			{
				// 传入发件人的姓名和授权码
				return new PasswordAuthentication(sendnameString, codeString);
			}
		});

		// 2、通过session获取transport对象
		Transport transport = session.getTransport();

		// 3、通过transport对象邮箱用户名和授权码连接邮箱服务器
		transport.connect("smtp.qq.com", sendnameString, codeString);

		// 4、创建邮件,传入session对象
		MimeMessage mimeMessage = complexEmail(session);

		// 5、发送邮件
		transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());

		// 6、关闭连接
		transport.close();
		System.out.println("发送成功");

	}

	public static MimeMessage complexEmail(Session session) throws MessagingException
	{

		// 消息的固定信息
		MimeMessage mimeMessage = new MimeMessage(session);

		// 发件人
		mimeMessage.setFrom(new InternetAddress(sendnameString));
		// 收件人
		mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(getNameString));
		// 邮件标题
		mimeMessage.setSubject(headTitleString);

		// 邮件内容
		// 准备文本
		MimeBodyPart text = new MimeBodyPart();
		// text.setContent("这是一段文本<img src='cid:test.png'>",
		// "text/html;charset=utf-8");
		text.setContent(contextString, "text/html;charset=utf-8");
		// 附件
		MimeBodyPart appendix = new MimeBodyPart();
		appendix.setDataHandler(new DataHandler(new FileDataSource(fileString)));
		String[] filename = fileString.split("\\\\");
		appendix.setFileName(filename[filename.length - 1]);

		// 准备图片数据
		MimeBodyPart image = new MimeBodyPart();
		DataHandler handler = new DataHandler(new FileDataSource(imgUrlString));
		image.setDataHandler(handler);
		image.setContentID("test.png"); // 设置图片id

		// 拼装邮件正文
		MimeMultipart mimeMultipart = new MimeMultipart();
		mimeMultipart.addBodyPart(image);
		mimeMultipart.addBodyPart(text);
		mimeMultipart.setSubType("related");// 文本和图片内嵌成功

		// 将拼装好的正文内容设置为主体
		MimeBodyPart contentText = new MimeBodyPart();
		contentText.setContent(mimeMultipart);

		// 拼接附件
		MimeMultipart allFile = new MimeMultipart();
		allFile.addBodyPart(appendix);// 附件
		allFile.addBodyPart(contentText);// 正文
		allFile.setSubType("mixed"); // 正文和附件都存在邮件中，所有类型设置为mixed

		// 放到Message消息中
		mimeMessage.setContent(allFile);
		mimeMessage.saveChanges();// 保存修改

		return mimeMessage;
	}

}
