package chatroom;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebCharServerInitialize extends ChannelInitializer<SocketChannel> {

	protected void initChannel(SocketChannel socketChannel) throws Exception {
		// TODO Auto-generated method stub
		//进行管道式数据处理
		 ChannelPipeline pipeline=socketChannel.pipeline();
		 
		 pipeline.addLast(new HttpServerCodec())//第一道工序：将请求和应答消息编码或者解码为http协议的消息
		 		  .addLast(new HttpObjectAggregator(64*1024))//第二道工序：限制通道的大小
		 		  .addLast(new ChunkedWriteHandler())//负责向客户端发送html的页面文件
		 		  .addLast(new HttpRequestHandler("/chat"))//加载页面
		 		  .addLast(new WebSocketServerProtocolHandler("/chat"))//处理websocket协议
		 		  .addLast(new TextWebSocketFrameHandler());
		
	}
	
}
