package com.buntoy.rejecttool.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

public class UiFrame {

	public static BigInteger GAS_PRICE = BigInteger.valueOf(150_000_000_000L); // 250
																				// 000
	public static BigInteger GAS_LIMIT = BigInteger.valueOf(30_000L);

	public static BigDecimal gwei = BigDecimal.valueOf(1_000_000_000L);

	public static Web3j web3j = Web3j.build(new HttpService("https://mainnet.infura.io/"));// https://ropsten.infura.io/;

	public static JFrame placeComponents() {
		// 创建 JFrame 实例
		JFrame frame = new JFrame("ETH Reject Tool By buntoy.com");
		// Setting the width and height of frame
		frame.setBounds(700, 40, 700, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		/*
		 * 创建面板，这个类似于 HTML 的 div 标签 我们可以创建多个面板并在 JFrame 中指定位置
		 * 面板中我们可以添加文本字段，按钮及其他组件。
		 */
		JPanel panel = new JPanel();

		JLabel linklabel = new JLabel("<html><a href='https://www.buntoy.com'> buntoy.com </a></html>");  
		linklabel.setBounds(10, 200, 500, 25);
        linklabel.addMouseListener(new MouseAdapter() {  
            public void mouseClicked(MouseEvent e) {  
                try {  
                    Runtime.getRuntime().exec("cmd.exe /c start " + "https://www.buntoy.com");  
                } catch (Exception ex) {  
                    ex.printStackTrace();  
                }  
            }  
        }); 
        
        JLabel gitlabel = new JLabel("<html><a href='https://github.com/BUN-VictorJiang/ETH_RejectTool'> GitHub </a></html>");  
		gitlabel.setBounds(600, 200, 500, 25);
        gitlabel.addMouseListener(new MouseAdapter() {  
            public void mouseClicked(MouseEvent e) {  
                try {  
                    Runtime.getRuntime().exec("cmd.exe /c start " + "https://github.com/BUN-VictorJiang/ETH_RejectTool");  
                } catch (Exception ex) {  
                    ex.printStackTrace();  
                }  
            }  
        }); 
        panel.add(linklabel);
        panel.add(gitlabel);
		// 添加面板
		frame.add(panel);
	//	panel.add
		/*
		 * 布局部分我们这边不多做介绍 这边设置布局为 null
		 */
		panel.setLayout(null);
		
		//输入框
		JLabel privateKeyLab = new JLabel("私钥：");
		privateKeyLab.setBounds(10,20,120,25);
		panel.add(privateKeyLab);
		JTextField privateKeyText = new JTextField(20);
		privateKeyText.setBounds(100,20,500,25);
		panel.add(privateKeyText);
		
		JLabel nonceLab = new JLabel("nonce：");
		nonceLab.setBounds(10,50,120,25);
		panel.add(nonceLab);
		JTextField nonceText = new JTextField(20);
		nonceText.setBounds(100,50,500,25);
		panel.add(nonceText);
		
		JLabel gasPriceLab = new JLabel("单价gwei：");
		gasPriceLab.setBounds(10,80,120,25);
		panel.add(gasPriceLab);
		JTextField gasText = new JTextField(20);
		gasText.setBounds(100,80,500,25);
		panel.add(gasText);

		// 创建登录按钮
		JButton button1 = new JButton("撤销 Reject");
		button1.setBounds(10, 120, 120, 25);
		panel.add(button1);
		

		JLabel txHashLab = new JLabel("TxHash：");
		txHashLab.setBounds(10, 160, 500, 25);
		panel.add(txHashLab);
		
		JTextField txHashText = new JTextField();
		txHashText.setBounds(100, 160, 500, 25);
		txHashText.setEditable(false);
		panel.add(txHashText);
		
		button1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				button1.setEnabled(false);
				
				
				String privateKey = privateKeyText.getText().trim();
				
				BigInteger nonce = new BigInteger(nonceText.getText().trim());
				
				BigDecimal priceGWEI = new BigDecimal(gasText.getText().trim());
				BigInteger gasPrice = priceGWEI.multiply(gwei).toBigInteger();
				
				System.out.println("price: "+gasPrice.toString());
				String txHash = sendEthSelf(web3j, privateKey, nonce, gasPrice);
				System.out.println("txHash: "+txHash);
				
				txHashText.setText(txHash);
				
				button1.setEnabled(true);
			}
		});

		return frame;

	}
	
	public static String sendEthSelf(Web3j web3j, String privateKey, BigInteger nonce, BigInteger gasPrice) {
		Credentials credentials = Credentials.create(privateKey);
		try {
			/*TransactionReceipt transactionReceipt =Transfer.sendFunds(
			        web3, credentials, credentials.getAddress(), BigDecimal.ZERO, Convert.Unit.ETHER)
			        .send();
			
			String txHash = transactionReceipt.getTransactionHash();
			
			*/
			
			RawTransaction rawTransaction  = RawTransaction.createEtherTransaction(
					nonce, 
					gasPrice, 
					GAS_LIMIT, 
					credentials.getAddress(), 
					BigInteger.ZERO);
			RawTransactionManager rawManager = new RawTransactionManager(web3j, credentials);
			
			EthSendTransaction ethSendTransaction = rawManager.signAndSend(rawTransaction);
			
			
			return ethSendTransaction.getTransactionHash();
		} catch (Exception e) {
			e.printStackTrace(); 
			return null;
		}
	}
}