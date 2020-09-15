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
        <meta-data android:name="com.skyworthiot.sselib.APPKEY" android:value= ""/> 
        <meta-data android:name="com.skyworthiot.sselib.HOSTADDRESS" android:value="https://eduapi.skyworthiot.com"/>
~~~

- 5 实现IotSSEMsgLib.IOTSSEMsgListener的监听,在监听代码中的实现接口中填如saltkey．
~~~ java
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
~~~ java
	/**
     * 接入到IOT的SSE平台
     *   默认从配置文件读取application（区域） xml当中的配置文件，请务必填写，否则无法使用
     *   <meta-data
     *      android:name="com.skyworthiot.sselib.APPKEY"
     *      android:value="xxx"/>
     *
     *  各个业务平台业务增长迅猛，可能会挤占其他业务的服务器资源，需要做业务切割，针对每个业务的费用单独计费
     *  比如，教育小微/少儿影视小维就是如下的host地址： https://eduapi.skyworthiot.com
     *   <meta-data
     *      android:name="com.skyworthiot.sselib.HOSTADDRESS"
     *      android:value="https://eduapi.skyworthiot.com"/>
     *
     * @param context android执行环境上下文
     * @param listener IOTSSEMsgListener 回调，sse执行过程的回调
     */
    public IotSSEMsgLib(Context context,IOTSSEMsgListener listener);
~~~

~~~ java
 /**
     * 接入到IOT的SSE平台
     *   默认从配置文件读取application（区域） xml当中的配置文件，请务必填写，否则无法使用
     *   <meta-data
     *      android:name="com.skyworthiot.sselib.APPKEY"
     *      android:value="xxx"/>
     *
     *  各个业务平台业务增长迅猛，可能会挤占其他业务的服务器资源，需要做业务切割，针对每个业务的费用单独计费
     *  比如，教育小微/少儿影视小维就是如下的host地址： https://eduapi.skyworthiot.com
     *   <meta-data
     *      android:name="com.skyworthiot.sselib.HOSTADDRESS"
     *      android:value="https://eduapi.skyworthiot.com"/>
     *
     * @param context android执行环境上下文
     * @param listener IOTSSEMsgListener 回调，sse执行过程的回调
     */
    public IotSSEMsgLib(Context context,IOTSSEMsgListener_WMP listener)
~~~

~~~ java
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

~~~ java
	 /**
     * 获得连接到SSE平台的唯一ID
     * @return
     */
    public String readUniqueID()；
~~~

~~~ java
	 /**
     * 主动关闭SSE通道
     * @return
     */
    public void close()
~~~

~~~ java
	/**
     * 连接SSE平台
     * 标准接口，需要根据业务需要传递唯一ID
     * @param did 链接到IOTSSE平台的did 必须唯一，本机唯一did
     * @return ture 成功,false 失败
     */
    public boolean connectSSE(String did)
~~~

~~~ java
	 /**
     * 重新连接SSE平台
     * 在设备上的网络状态发生变化的时候，或者唯一的设备ID发生了变化，
     * 请重新调用该接口
     * @param did
     * @return
     */
    public boolean reConnectSSE(String did)
~~~
~~~ java
 /**
     * 以智慧屏的方式接入SSE
     * 智慧屏的接入方式相比connectSSE接口，
     * 相比connectSSE接口，云端会增加智慧屏好友列表设备在离线状态的消息推送，以及智慧屏业务相关的其他消息推送
     * 用于PAD系统及酷开8.x系统上，普通第三方请勿使用
     * @param screenID
     * @return
     */
    public boolean connectSSEAsSmartScreen(String screenID)
~~~

~~~ java
	  /**
     * @return true SSE已经连接成功,false 失败
     */
    public boolean isSSEConnected()；
~~~
~~~ java
    /**
     * 重新连接智慧屏SSE平台
     * 在设备上的网络状态发生变化的时候，或者ScreenID发生变化的时候，
     * 请重新调用该接口
     * @param screenID
     * @return
     */
    public boolean reConnectSSEAsSmartScreen(String screenID)
~~~

~~~ java
    /**
     * 以 Swaiot-Iot设备的方式接入SSE
     * Swaiot-Iot设备的接入方式相比connectSSE接口，会增加用户账号下的其他智能硬件设备状态变化的推送
     * 可以用于小维智联监听所有账号下的其他IOT设备状态变化的推送情况
     * @param did Iot设备标识
     * @param accessToken swiaot-酷开账号系统获得的accessToken
     * @param uid swaiot-酷开账号系统获得的uid
     * @return
     */
    public boolean connectSSEAsIotDevice(String did,String accessToken,String uid)
~~~
~~~ java
   /**
     * 以Swaiot-Iot设备的方式重新接入SSE平台
     * 在设备上的网络状态发生变化的时候，请重新调用该接口
     * @param did Iot设备标识
     * @return
     */
    public boolean reConnectSSEAsIotDevice(String did,String accessToken,String uid)
~~~

~~~ java
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
~~~ java
 /**
     * 发送消息给小程序，由于小程序业务拿不到原始的did，单独封装发送接口，再转web-socket转发小程序
     * 单独封装接口，后台业务进行对接，用于小程序业务,正常业务请勿调用，会发送失败
     * @param msgId 发送的消息ｉｄ
     * @param msgName　发送的消息名称
     * @param message　发送的消息
     * @return true 成功加入发送队列，false 加入发送队列失败
     */
    public boolean sendMessageWeChatMiniProg(String msgId,String msgName,String message)
~~~

~~~ java
	/**
     * 该接口为异步接口，可以同时发给多个设备不同的文件。
     * 发送过程由onSendFileToCloud 进行回调
     * @param toDestDid 发送文件给到的目标设备id
     * @param sendFile 要发送的文件
     * @param uid，用来标识文件上传方的id，建议传入connectSSE方法的did参数．
     */
    public void syncFileToCloud(String toDestDid, File sendFile,String uid)；
~~~
~~~ java
	/**
     * 从云端同步文件，该接口为异步接口，可同时同步多个文件
     * @param fileKey onSendFileToCloud 完成时候，回调的fileKey
	 * 该fileKey是发送方调用syncFileToCloud之后接受到的消息，然后发送方再调用sendMessage接口将fileKey传递给消息接收方，接收方完成文件下载．会将文件存储于应用纱盒的cache目录
     */
    public void syncFileFromCloud(String fileKey)
~~~

###### IotSSEMsgLib.IOTSSEMsgListener_WMP
~~~ java
 /**
* 小程序业务没有真实的目标id，所以增加了一个监听接口继承于IOTSSEMsgListener
* 小程序相关的业务在初始化IotSSEMsgLib类的时候，请传递该listner，否则发送结果无法监听
         * 发送SSE消息给到小程序，是否成功的回调
         * @param result　是否在线，代表接口成功，并非小程序真实在线，仅仅是后台业务与小程序业务完成了调用逻辑。
         * @param msgId 发送的消息id
         * @param msgName 发送的消息名称
         * @param message　发生的消息
         */
 void onSendWMPResult(SSESendResultEnum result,String msgId,String msgName,String message);
~~~

###### IotSSEMsgLib.IOTSSEMsgListener 回调API
~~~ java
	/**
         *　由于apk反编译之后，容易从AndroidMenifest.xml读取到ａｋ和ｓｋ，其中ａｋ是公开的，
         *  sk是程序私有的，不能被第三方获取。最好写在混淆的程序当中。
         * @return　写在程序当中的appsalt
         */
        String appSalt();
~~~
~~~ java
       /**
         * @param errEnum
         *        收到该错误的所有讯息，代表通道创建失败，需要关注失败的原因，
         *        如果是网络问题，那么需要接收错误回调并制定自身app的重连机制和策略。
         * **请注意，重联策略需要1-3-5-7-9...2min然后进行重联尝试**，否则会被后台认定ddos攻击屏蔽请求。
         *  SDK代码中判断和connectThread在同一个线程里面调用reConnect ，则直接返回false，避免while卡死的问题，
         *  推荐用demo的方法去做重联
         */
        void onSSELibError(SSEErrorEnum errEnum,String errMessage);
~~~
~~~ java
        /**
         * 调用connectSSE　成功之后，会回调该接口
         */
        void onSSEStarted();
~~~
~~~ java
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
~~~ java
        /**
         * 接收到其他设备发过来的消息
         * @param msgId 　消息ｉｄ
         * @param msgName　消息名称
         * @param message　消息
         */
        void onReceivedSSEMessage(String msgId,String msgName,String message);
~~~
~~~ java
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
~~~ java
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