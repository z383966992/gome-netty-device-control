package gome.netty.device.control;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;

import com.gome.netty.codec.MarshallingCodecFactory;
import com.gome.netty.struct.Message;
import com.gome.netty.struct.MessageType;

public class EventServerClient {

    private String result = null;

    public void sendToEventServer(String ipInfo, final int timeOut) {
        String[] s = ipInfo.split(":");
        final String host = s[0];
        final int port = Integer.parseInt(s[1]);
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new ReadTimeoutHandler(timeOut));
                            pipeline.addLast("decoder", MarshallingCodecFactory.buildMarshallingDecoder());
                            pipeline.addLast("encoder", MarshallingCodecFactory.buildMarshallingEncoder());
                            pipeline.addLast(new ChannelHandlerAdapter() {
                            	/**
                            	 * 发送控制命令给服务器端，由服务器端转发给客户端
                            	 */
                            	@Override
                            	public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            		Message message = new Message();
                            		message.setContent("control command!");
                            		message.setMessageType(MessageType.CONTROL_COMMAND.value());
                            		message.setChannelId("12345678");
                            		ctx.writeAndFlush(message);
                            	}
                            	
                            	@Override
								public void channelRead(ChannelHandlerContext ctx, Object msg)
										throws Exception {
                            		Message message = (Message)msg;
                            		result = message.getContent();
                                  ctx.close();
								}

                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx,
                                                            Throwable cause) throws Exception {
                                    result = null;
                                    ctx.close();
                                }
                            });
                        }
                    });
                Channel ch = b.connect(host, port).sync().channel();
                ch.closeFuture().sync();
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void sendToEventServer(String ipInfo, String msg) {
        sendToEventServer(ipInfo, 2000);
    }

    public String msgReceived() {
        return result;
    }

    public static void main(String[] args) {
    	EventServerClient client = new EventServerClient();
		client.sendToEventServer("localhost:20000", "procedure,0001,device message");
		System.out.println(client.msgReceived());
	}
}