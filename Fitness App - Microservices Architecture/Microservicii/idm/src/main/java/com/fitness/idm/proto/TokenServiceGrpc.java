package com.fitness.idm.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.4.0)",
    comments = "Source: idm.proto")
public final class TokenServiceGrpc {

  private TokenServiceGrpc() {}

  public static final String SERVICE_NAME = "com.fitness.idm.proto.TokenService";

  // Static method descriptors that strictly reflect the proto.
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<TokenValidateRequest,
      TokenValidateResponse> METHOD_VALIDATE_TOKEN =
      io.grpc.MethodDescriptor.<TokenValidateRequest, TokenValidateResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.fitness.idm.proto.TokenService", "validateToken"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              TokenValidateRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              TokenValidateResponse.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<CredentialsRequest,
      TokenValidateRequest> METHOD_LOGIN =
      io.grpc.MethodDescriptor.<CredentialsRequest, TokenValidateRequest>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.fitness.idm.proto.TokenService", "login"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              CredentialsRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              TokenValidateRequest.getDefaultInstance()))
          .build();
  @io.grpc.ExperimentalApi("https://github.com/grpc/grpc-java/issues/1901")
  public static final io.grpc.MethodDescriptor<TokenValidateRequest,
      EmptyGrpcResponse> METHOD_LOGOUT =
      io.grpc.MethodDescriptor.<TokenValidateRequest, EmptyGrpcResponse>newBuilder()
          .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
          .setFullMethodName(generateFullMethodName(
              "com.fitness.idm.proto.TokenService", "logout"))
          .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              TokenValidateRequest.getDefaultInstance()))
          .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
              EmptyGrpcResponse.getDefaultInstance()))
          .build();

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static TokenServiceStub newStub(io.grpc.Channel channel) {
    return new TokenServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static TokenServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new TokenServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static TokenServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new TokenServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class TokenServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void validateToken(TokenValidateRequest request,
                              io.grpc.stub.StreamObserver<TokenValidateResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_VALIDATE_TOKEN, responseObserver);
    }

    /**
     */
    public void login(CredentialsRequest request,
                      io.grpc.stub.StreamObserver<TokenValidateRequest> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LOGIN, responseObserver);
    }

    /**
     */
    public void logout(TokenValidateRequest request,
                       io.grpc.stub.StreamObserver<EmptyGrpcResponse> responseObserver) {
      asyncUnimplementedUnaryCall(METHOD_LOGOUT, responseObserver);
    }

    @Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            METHOD_VALIDATE_TOKEN,
            asyncUnaryCall(
              new MethodHandlers<
                TokenValidateRequest,
                TokenValidateResponse>(
                  this, METHODID_VALIDATE_TOKEN)))
          .addMethod(
            METHOD_LOGIN,
            asyncUnaryCall(
              new MethodHandlers<
                CredentialsRequest,
                TokenValidateRequest>(
                  this, METHODID_LOGIN)))
          .addMethod(
            METHOD_LOGOUT,
            asyncUnaryCall(
              new MethodHandlers<
                TokenValidateRequest,
                EmptyGrpcResponse>(
                  this, METHODID_LOGOUT)))
          .build();
    }
  }

  /**
   */
  public static final class TokenServiceStub extends io.grpc.stub.AbstractStub<TokenServiceStub> {
    private TokenServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TokenServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected TokenServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TokenServiceStub(channel, callOptions);
    }

    /**
     */
    public void validateToken(TokenValidateRequest request,
                              io.grpc.stub.StreamObserver<TokenValidateResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_VALIDATE_TOKEN, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void login(CredentialsRequest request,
                      io.grpc.stub.StreamObserver<TokenValidateRequest> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LOGIN, getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void logout(TokenValidateRequest request,
                       io.grpc.stub.StreamObserver<EmptyGrpcResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(METHOD_LOGOUT, getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class TokenServiceBlockingStub extends io.grpc.stub.AbstractStub<TokenServiceBlockingStub> {
    private TokenServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TokenServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected TokenServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TokenServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public TokenValidateResponse validateToken(TokenValidateRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_VALIDATE_TOKEN, getCallOptions(), request);
    }

    /**
     */
    public TokenValidateRequest login(CredentialsRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LOGIN, getCallOptions(), request);
    }

    /**
     */
    public EmptyGrpcResponse logout(TokenValidateRequest request) {
      return blockingUnaryCall(
          getChannel(), METHOD_LOGOUT, getCallOptions(), request);
    }
  }

  /**
   */
  public static final class TokenServiceFutureStub extends io.grpc.stub.AbstractStub<TokenServiceFutureStub> {
    private TokenServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private TokenServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @Override
    protected TokenServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new TokenServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<TokenValidateResponse> validateToken(
        TokenValidateRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_VALIDATE_TOKEN, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<TokenValidateRequest> login(
        CredentialsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LOGIN, getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<EmptyGrpcResponse> logout(
        TokenValidateRequest request) {
      return futureUnaryCall(
          getChannel().newCall(METHOD_LOGOUT, getCallOptions()), request);
    }
  }

  private static final int METHODID_VALIDATE_TOKEN = 0;
  private static final int METHODID_LOGIN = 1;
  private static final int METHODID_LOGOUT = 2;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final TokenServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(TokenServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_VALIDATE_TOKEN:
          serviceImpl.validateToken((TokenValidateRequest) request,
              (io.grpc.stub.StreamObserver<TokenValidateResponse>) responseObserver);
          break;
        case METHODID_LOGIN:
          serviceImpl.login((CredentialsRequest) request,
              (io.grpc.stub.StreamObserver<TokenValidateRequest>) responseObserver);
          break;
        case METHODID_LOGOUT:
          serviceImpl.logout((TokenValidateRequest) request,
              (io.grpc.stub.StreamObserver<EmptyGrpcResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @Override
    @SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static final class TokenServiceDescriptorSupplier implements io.grpc.protobuf.ProtoFileDescriptorSupplier {
    @Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return Idm.getDescriptor();
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (TokenServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new TokenServiceDescriptorSupplier())
              .addMethod(METHOD_VALIDATE_TOKEN)
              .addMethod(METHOD_LOGIN)
              .addMethod(METHOD_LOGOUT)
              .build();
        }
      }
    }
    return result;
  }
}
