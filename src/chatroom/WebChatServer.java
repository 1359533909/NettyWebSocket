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
	//构造函数
	public WebChatServer(int port) {
		this.port=port;
	}
	public void start() {
		//定义两个线程组
		//nio中是一个队列，不断的有请求进入到队列中，然后将以队列的机制进行处理
		EventLoopGroup boss=new NioEventLoopGroup();//负责客户端的接入
		EventLoopGroup worker=new NioEventLoopGroup();//负责工作（客户端的网络请求）的线程
		 try {
		//对客户端的行为就行定义
		ServerBootstrap bootstrap=new ServerBootstrap();
		//绑定线程组
		bootstrap.group(boss, worker)
				 .channel(NioServerSocketChannel.class) //指定以nio的请求方式
				 .childHandler(new WebCharServerInitialize())//处理服务器中的数据
				 .option(ChannelOption.SO_BACKLOG,128 )//配置参数128是官方配置参数
				 .childOption(ChannelOption.SO_KEEPALIVE, true);//保持连接 ;
			ChannelFuture future=bootstrap.bind(port).sync();
			System.out.println("[系统消息]:服务器已经启动");
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
