package chatroom;

import java.io.File;
import java.io.RandomAccessFile;
import java.net.URISyntaxException;
import java.net.URL;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedNioFile;

public class HttpRequestHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	
	private final String chatUri;
	private static File indexFile;
	public HttpRequestHandler(String chatUri) {
		this.chatUri=chatUri;
	}
	static {
		//获取当前地址
		
		try {
//			URL location=HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
//			String path=location.toURI().getPath()+"index.html";
			URL location=HttpRequestHandler.class.getProtectionDomain().getCodeSource().getLocation();
//			String path="D:/eclipse-workspace/NettyWebSocket/WebContent/index.html";
//			indexFile=new File(path);
//			System.out.println(path);
			File pathIndex=new File("WebContent\\index.html");
			System.out.println("路径位置："+pathIndex.getAbsolutePath().replace("\\", "/"));
			indexFile=new File(pathIndex.getAbsolutePath().replace("\\", "/"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		// TODO Auto-generated method stub
		System.out.println(request.getUri());
		if(chatUri.equalsIgnoreCase(request.getUri())) {
			System.out.println("请求为websocket请求");
			ctx.fireChannelRead(request.retain());//如果是websocket请求，就将其交给下一道工序
		}else {
			System.out.println("请求是http请求，则需要读取index.html页面并发送给客户端浏览器");
			if(HttpHeaders.is100ContinueExpected(request)) {
				//连接是100，处理完成是200
				FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE);
				ctx.writeAndFlush(response);
			}
			//读取默认的index.html页面，使用io读取也是一样的
			RandomAccessFile file=new RandomAccessFile(indexFile, "r");//"r"为读取模式
			//设置http协议的响应头
			HttpResponse response=new DefaultHttpResponse(request.getProtocolVersion(), HttpResponseStatus.OK);//ok代表200的意思
			response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");
			boolean keepAlive=HttpHeaders.isKeepAlive(request);
			if(keepAlive)
			{
				response.headers().set(HttpHeaders.Names.CONTENT_LENGTH,file.length());
				response.headers().set(HttpHeaders.Names.CONNECTION,HttpHeaders.Values.KEEP_ALIVE);
			}
			ctx.write(response);
			//将html写到客户端
			ctx.write(new ChunkedNioFile(file.getChannel()));
			ChannelFuture future=ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
			if(!keepAlive) {
				future.addListener(ChannelFutureListener.CLOSE);
			}
			file.close();
		}
	}

}
