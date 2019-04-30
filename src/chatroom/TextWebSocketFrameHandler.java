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

	private static ChannelGroup chanels=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);//管理所有连接上来的用户
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
		
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();//获得发送信息的连接通道
		for(Channel ch:chanels) {
			if(ch!=incoming) {
				ch.writeAndFlush(new TextWebSocketFrame("[用户"+incoming.remoteAddress()+"说：]"+msg.text()+"\n"));
			}else {
				ch.writeAndFlush(new TextWebSocketFrame("[我说："+msg.text()+"\n"));
			}
		}
		
	}
//当有用户连接时会自动调用
	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		
		//进入聊天室
		for(Channel ch:chanels)
		{
			if(ch!=incoming) {
				ch.writeAndFlush(new TextWebSocketFrame("欢迎"+incoming.remoteAddress()+"进入聊天室"+"\n"));
			}
		}
		chanels.add(incoming);
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		// TODO Auto-generated method stub
		Channel incoming=ctx.channel();
		
		//进入聊天室
//		for(Channel ch:chanels)
//		{
//			if(ch!=incoming) {
//				ch.writeAndFlush(new TextWebSocketFrame(incoming.remoteAddress()+"离开聊天室"+"\n"));
//			}
//		}
		chanels.remove(incoming);
	}



}
