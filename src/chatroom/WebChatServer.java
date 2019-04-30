package chatroom;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class WebChatServer {
	private int port;
	//���캯��
	public WebChatServer(int port) {
		this.port=port;
	}
	public void start() {
		//���������߳���
		//nio����һ�����У����ϵ���������뵽�����У�Ȼ���Զ��еĻ��ƽ��д���
		EventLoopGroup boss=new NioEventLoopGroup();//����ͻ��˵Ľ���
		EventLoopGroup worker=new NioEventLoopGroup();//���������ͻ��˵��������󣩵��߳�
		 try {
		//�Կͻ��˵���Ϊ���ж���
		ServerBootstrap bootstrap=new ServerBootstrap();
		//���߳���
		bootstrap.group(boss, worker)
				 .channel(NioServerSocketChannel.class) //ָ����nio������ʽ
				 .childHandler(new WebCharServerInitialize())//����������е�����
				 .option(ChannelOption.SO_BACKLOG,128 )//���ò���128�ǹٷ����ò���
				 .childOption(ChannelOption.SO_KEEPALIVE, true);//�������� ;
			ChannelFuture future=bootstrap.bind(port).sync();
			System.out.println("[ϵͳ��Ϣ]:�������Ѿ�����");
			future.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		new WebChatServer(9080).start();
	}

}
