#### Swaiot开放平台下推送SDK库

##### SDK下载地址
https://github.com/swaiot/SSE-SDK-Android

##### 安装方式
- 1 在开放平台上获得分配到的Swaiot开发者appkey和saltkey;
- 2 在工程的libs中引入sselib_v1.0_2020-04-09-12-18_release.aar ;
- 3 在工程的build.gradle当中增加如下:
~~~ java
    implementation 'com.squareup.okhttp3:okhttp:3.9.0'
    implementation 'com.squareup.okio:okio:1.13.0'
~~~
> SDK集成了第三方文件上传业务(百度Bos)，Swaiot开放平台会根据不同的开发者appkey生成不同的文件存储桶，该部分功能由于第三方文件上传业务需要收费，所以Swaiot开放平台会按照第三方云业务的标准向开发者进行收费．

- 4 在工程AndroidManifest.xml代码中修改:
~~~ java
<!--"FIXME: 此处从swaiot开放平台获得分配给到应用开发者的appkey"-->
        <meta-data android:name="com.skyworthiot.sselib.APPKEY" android:value= ""/> -->
~~~

- 5 实现IotSSEMsgLib.IOTSSEMsgListener的监听,在监听代码中的实现接口中填如saltkey．
~~~java
	/**
         *　由于apk反编译之后，容易从AndroidMenifest.xml读取到ａｋ和ｓｋ，其中ａｋ是公开的，
         *  sk是程序私有的，不能被第三方获取。最好写在混淆的程序当中。
         * @return　写在程序当中的appsalt
         */
        @Override
        public String appSalt() {
            return "";
        }
~~~

详细可见下载地址中的demo工程的配置及代码说明.

##### API说明

###### IotSSEMsgLib 类API
~~~java
	/**
     * 接入到IOT的SSE平台
     *   默认从配置文件读取application（区域） xml当中的配置文件，请务必填写，否则无法使用
     *   <meta-data
     *      android:name="com.skyworthiot.sselib.APPKEY"
     *      android:value="xxx"/>
     * @param context  Android上下文Context
	 * @param listener   IotSSEMsgLib.IOTSSEMsgListener的监听对象
     */
    public IotSSEMsgLib(Context context,IOTSSEMsgListener listener);
~~~

~~~java
	/**
     * 自动生成UUID作为唯一标识，连接SSE平台
     * @return ture 成功,false 失败
     */
    public boolean connectSSE()；
~~~

~~~java
	 /**
     * 重新连接SSE平台
     * 采用自动生成的UUID作为唯一标识进行重连接
     * @return ture 成功,false 失败
     */
    public boolean reConnectSSE()；
~~~

~~~java
	 /**
     * 获得连接到SSE平台的唯一ID
     * @return
     */
    public String readUniqueID()；
~~~

~~~java
	/**
     * 注册监听该用户下的所有iot设备的消息
     * 该消息能够帮助APP接收到用于账号下的所有Swaiot-IOT设备状态变化的消息
     * 会从onReceivedMessage方法中回调
     * @param userID 从账号SDK当中获取到的用户uid
     * @return
     */
    public void registerListenUsersIotMessage(final String userID);
~~~

~~~java
	/**
	 * 该方法需要APP自己管理运行在的设备上生成的id，建议使用上述两个接口,并从readUniqueID获得SDK给APP生成的ID，不要调用该方法；
     * 连接SSE平台
     * @param did 链接到IOTSSE平台的did 必须唯一，本机唯一did
     * @param uid 链接到IOTSSE平台的uid，swaiot生态用户唯一uid,可以为空
     * @return ture 成功,false 失败
     */
    public boolean connectSSE(String did,String uid);
~~~

~~~java
	/**
	* 该方法需要APP自己管理运行在的设备上生成的id，建议使用上述两个接口,并从readUniqueID获得SDK给APP生成的ID，不要调用该方法；
     * 重新连接SSE平台
     * 在设备上的uid发生变化的时候，请调用该方法
     * @param did　必传，用于唯一标识设备消息接收，全网唯一ID
     * @param uid　可以为空，如果不为空，则发送方发送的消息给设备需要did&uid的完整组合才能确保接收到消息
     * @return
     */
    public boolean reConnectSSE(String did,String uid)；
~~~
~~~java
	  /**
     * @return true SSE已经连接成功,false 失败
     */
    public boolean isSSEConnected()；
~~~
~~~java
	/**
     * 发送消息给其他设备
     * @param toDestDid 发送到的设备ｉd
     * @param msgId 发送的消息ｉｄ
     * @param msgName　发送的消息名称
     * @param message　发送的消息
     * @return true 成功加入发送队列，false 加入发送队列失败
     */
    public boolean sendMessage(String toDestDid,String msgId,String msgName,String message)；
~~~
~~~java
	/**
     * 该接口为异步接口，可以同时发给多个设备不同的文件。
     * 发送过程由onSendFileToCloud 进行回调
     * @param toDestDid 发送文件给到的目标设备id
     * @param sendFile 要发送的文件
     * @param uid，用来标识文件上传方的id，建议传入connectSSE方法的did参数．
     */
    public void syncFileToCloud(String toDestDid, File sendFile,String uid)；
~~~
~~~java
	/**
     * 从云端同步文件，该接口为异步接口，可同时同步多个文件
     * @param fileKey onSendFileToCloud 完成时候，回调的fileKey
	 * 该fileKey是发送方调用syncFileToCloud之后接受到的消息，然后发送方再调用sendMessage接口将fileKey传递给消息接收方，接收方完成文件下载．会将文件存储于应用纱盒的cache目录
     */
    public void syncFileFromCloud(String fileKey)
~~~

###### IotSSEMsgLib.IOTSSEMsgListener 回调API
~~~java
	/**
         *　由于apk反编译之后，容易从AndroidMenifest.xml读取到ａｋ和ｓｋ，其中ａｋ是公开的，
         *  sk是程序私有的，不能被第三方获取。最好写在混淆的程序当中。
         * @return　写在程序当中的appsalt
         */
        String appSalt();
~~~
~~~java
        /**
         * @param errEnum
         *        错误为ConnectHostError的时候，表示无法访问网络，这种情况下一般是代表https接口请求错误；
         *        错误为SSEDisconnectError的时候，表示SSE连接断开，APP需要重新判断网络状态并调用connect 接口
         *        错误为SSESendError的时候，表示SSE发送失败，可能是当前帐号没有发送权限之类的
         */
        void onSSELibError(SSEErrorEnum errEnum,String errMessage);
~~~
~~~java
        /**
         * 调用connectSSE　成功之后，会回调该接口
         */
        void onSSEStarted();
~~~
~~~java
        /**
         * 发送SSE消息给到目标设备，是否成功的回调
         * @param sendResult　是否在线
         * @param destId　发送的目标设备id
         * @param msgId 发送的消息id
         * @param msgName 发送的消息名称
         * @param message　发生的消息
         */
        void onSendResult(SSESendResultEnum sendResult,String destId,String msgId,String msgName,String message);
~~~
~~~java
        /**
         * 接收到其他设备发过来的消息
         * @param msgId 　消息ｉｄ
         * @param msgName　消息名称
         * @param message　消息
         */
        void onReceivedSSEMessage(String msgId,String msgName,String message);
~~~
~~~java
        /**
         * 发送方回调传输文件的进度
         * @param sendFileResult 回调传输文件的result，SENDFILE_ERROR = 发送错误, SENDFILE_ONPROGRESS=正在发送进程, SENDFILE_FINISHED=发送完成
         * @param fileKey 传输的文件名称
         * @param currentSize SENDFILE_ONPROGRESS = 当前文件长度，SENDFILE_FINISHED= 0 , SENDFILE_ERROR=0
         * @param totalSize SENDFILE_ONPROGRESS = 当前文件总长度，SENDFILE_FINISHED=0, SENDFILE_ERROR=-1
         * @param respCode SENDFILE_ONPROGRESS =0,S ENDFILE_ERROR = -1 || HTTPSTATUS Error, SENDFILE_FINISHED = 200
         * @param respMsg SENDFILE_ONPROGRESS = “”，SENDFILE_ERROR = error message, SENDFILE_FINISHED = fileKey
         * @param toDestId SENDFILE_ONPROGRESS = real destId, SENDFILE_ERROR = real destId, SENDFILE_FINISHED = real destId
         */
        void onSendFileToCloud(SendFileResultEnum sendFileResult,String fileKey, long currentSize, long totalSize,int respCode,String respMsg,String toDestId);
~~~
~~~java
        /**
         * 接受方启动文件接收进程的回调
         * @param receFileResult 接收的结果
         * @param fileKey 文件key，包含文件类型/用户id/文件名称
         * @param currentSize 当前文件下载的size
         * @param totalSize 最终文件长度的size
         * @param url 本地文件路径
         */
        void onReceivedFileFromCloud(ReceivedFileResultEnum receFileResult,String fileKey,long currentSize, long totalSize,String url);
~~~
