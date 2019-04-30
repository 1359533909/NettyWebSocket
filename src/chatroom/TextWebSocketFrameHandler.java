package chatroom;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

public class TextWebSocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

	private static ChannelGroup chanels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);//�������������������û�
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();//��÷�����Ϣ������ͨ��
		for(Channel ch:chanels) {
			if(ch!=incoming) {
				ch.writeAndFlush(new TextWebSocketFrame("[�û�"+incoming.remoteAddress()+"˵��]"+msg.text()+"\n"));
			}else {
				ch.writeAndFlush(new TextWebSocketFrame("[��˵��"+msg.text()+"\n"));
			}
		}
		
	}
//�����û�����ʱ���Զ�����
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		
		//����������
		for(Channel ch:chanels)
		{
			if(ch!=incoming) {
				ch.writeAndFlush(new TextWebSocketFrame("��ӭ"+incoming.remoteAddress()+"����������"+"\n"));
			}
		}
		chanels.add(incoming);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		
		//����������
//		for(Channel ch:chanels)
//		{
//			if(ch!=incoming) {
//				ch.writeAndFlush(new TextWebSocketFrame(incoming.remoteAddress()+"�뿪������"+"\n"));
//			}
//		}
		chanels.remove(incoming);
	}



}
