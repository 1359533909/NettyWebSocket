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
		//���йܵ�ʽ���ݴ���
		 ChannelPipeline pipeline=socketChannel.pipeline();
		 
		 pipeline.addLast(new HttpServerCodec())//��һ�����򣺽������Ӧ����Ϣ������߽���ΪhttpЭ�����Ϣ
		 		  .addLast(new HttpObjectAggregator(64*1024))//�ڶ�����������ͨ���Ĵ�С
		 		  .addLast(new ChunkedWriteHandler())//������ͻ��˷���html��ҳ���ļ�
		 		  .addLast(new HttpRequestHandler("/chat"))//����ҳ��
		 		  .addLast(new WebSocketServerProtocolHandler("/chat"))//����websocketЭ��
		 		  .addLast(new TextWebSocketFrameHandler());
		
	}
	
}
