package org.westos.email;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

//发送邮件带大文件
public class sendEmailBigFile
{
	static String sendnameString = "110@qq.com";// 发送人邮箱
	static String codeString = "----";// 发送人授权码
	static String contextString = "java发送邮件";// 发送内容
	static String headTitleString = "java发送大文件";// 标题
	static String getNameString = "110@qq.com";// 收件人邮箱
	static String imgurlString = "C:\\Users\\123\\Pictures\\www.myhgh.club.png";// 图片地址

	public static void main(String[] args) throws Exception
	{

		// TODO Auto-generated method stub

		// 设置环境

		Properties pros = new Properties();// 实例化一个Properties对象

		pros.setProperty("mail.smtp.auth", "true");// 设置是否验证。此处为true

		pros.setProperty("mail.transport.protocol", "smtp");// 设置传输协议。此处为smtp协议

		Session session = Session.getInstance(pros);// 通过Session静态方法getInstance()传入一个Properties对象获取session对象

		session.setDebug(true);// 设置是否显示debug信息

		// 编辑好邮件信息

		Message msg = new MimeMessage(session);// 通过子类的构造函数传入一个session对象来实例化一个Message对象

		msg.setSubject(headTitleString);// 邮件标题

		msg.setFrom(new InternetAddress(sendnameString));// 发件人邮箱

		// 图片

		MimeBodyPart img = new MimeBodyPart();

		DataHandler dh = new DataHandler(new FileDataSource(imgurlString));// 图片路径

		img.setDataHandler(dh);

		img.setContentID("a");

		// 正文

		MimeBodyPart text = new MimeBodyPart();

		text.setContent(contextString + "<br/><img src='cid:a'>", "text/html;charset=utf-8"); // 注意编码问题

		// 描述数据关系

		MimeMultipart mm = new MimeMultipart();

		mm.addBodyPart(text);

		mm.addBodyPart(img);

		mm.setSubType("related");

		msg.setContent(mm);

		msg.saveChanges(); // 保存更新

		// 发送邮件

		Transport transport = session.getTransport();// 通过session的getTransport()方法获得Transport对象

		transport.connect("smtp.qq.com", 25, sendnameString, codeString);// 打开连接
		
		// 设置收件人邮箱并发送邮件
		transport.sendMessage(msg, new Address[] { new InternetAddress(getNameString) });

		transport.close();// 关闭连接

	}

}
