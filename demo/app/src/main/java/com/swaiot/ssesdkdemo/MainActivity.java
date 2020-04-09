package com.swaiot.ssesdkdemo;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.skyworthiot.iotssemsg.IotSSEMsgLib;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {


    /**
     *  sdk 监听函数
     * FIXME:
     * 开发者将从开放平台获得到的appSalt填入到appSalt()函数当中。
     * 所有的回调函数都运行于线程之中，请注意不要直接绘制主线程UI。
     */
    public class CustomIotSSEMsgListener implements IotSSEMsgLib.IOTSSEMsgListener{

        MainActivity mainActivity;
        public CustomIotSSEMsgListener(MainActivity activity){
            mainActivity = activity;
        }
        /**
         *　由于apk反编译之后，容易从AndroidMenifest.xml读取到ａｋ和ｓｋ，其中ａｋ是公开的，
         *  sk是程序私有的，不能被第三方获取。最好写在混淆的程序当中。
         * @return　写在程序当中的appsalt
         */
        @Override
        public String appSalt() {
            return "";
        }

        /**
         * @param errEnum
         *        错误为ConnectHostError的时候，表示无法访问网络，这种情况下一般是代表https接口请求错误；
         *        错误为SSEDisconnectError的时候，表示SSE连接断开，APP需要重新判断网络状态并调用connect 接口
         *        错误为SSESendError的时候，表示SSE发送失败，可能是当前帐号没有发送权限之类的
         * @param errMsg
         *        返回的错误消息
         */
        @Override
        public void onSSELibError(IotSSEMsgLib.SSEErrorEnum errEnum, String errMsg) {
            Log.i("sse-lib","sseErrorEnum = " + errEnum + " msg = "+ errMsg);
        }

        /**
         * 调用connectSSE　成功之后，会回调该接口
         */
        @Override
        public void onSSEStarted() {
            Log.i("sse-lib","onSSEStarted");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView txtView = mainActivity.findViewById(R.id.txt_contet);
                    boolean isConnect = mainActivity.isSSEConnected();
                    txtView.setText("connectSSE  finished ! " + isConnect);
                }
            });

        }

        /**
         * 发送SSE消息给到目标设备，是否成功的回调
         * @param sendResult　是否在线
         * @param destId　发送的目标设备id
         * @param msgId 发送的消息id
         * @param msgName 发送的消息名称
         * @param message　发生的消息
         */
        @Override
        public void onSendResult(IotSSEMsgLib.SSESendResultEnum sendResult, String destId, String msgId, String msgName, String message) {
            Log.i("sse-lib","onSendResult =" + sendResult + " destId=" +destId + " msgId="+msgId+" msgName="+msgName+" message= " +message );

        }

        /**
         * 接收到其他设备发过来的消息
         * @param msgId 　消息ｉｄ
         * @param msgName　消息名称
         * @param message　消息
         */
        @Override
        public void onReceivedSSEMessage(String msgId, String msgName, String message) {
            Log.i("sse-lib","onReceivedSSEMessage msgId=" +msgId + " msgName="+msgName+" message="+message );
        }

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
        @Override
        public void onSendFileToCloud(IotSSEMsgLib.SendFileResultEnum sendFileResult, String fileKey, long currentSize, long totalSize, int respCode, String respMsg, String toDestId) {
            Log.i("sse-lib","onSendFileToCloud sendFileResult="+sendFileResult +"  fileKey=" +fileKey + " currentSize="+currentSize+" totalSize="+totalSize +" respCode" +respCode +" respMsg" +respMsg +" toDestId" +toDestId );

        }

        /**
         * 接受方启动文件接收进程的回调
         * @param receFileResult 接收的结果
         * @param fileKey 文件key，包含文件类型/用户id/文件名称
         * @param currentSize 当前文件下载的size
         * @param totalSize 最终文件长度的size
         * @param url 本地文件路径
         */
        @Override
        public void onReceivedFileFromCloud(IotSSEMsgLib.ReceivedFileResultEnum receFileResult, String fileKey, long currentSize, long totalSize, String url) {
            Log.i("sse-lib","onReceivedFileFromCloud receFileResult="+receFileResult+"  fileKey=" +fileKey + " currentSize=" +currentSize +" totalSize="+totalSize +" url="+url);

        }
    }

    private IotSSEMsgLib iotSSE; // SSE实时消息通信库
    private CustomIotSSEMsgListener customerListener;// SSE实时消息通信库回调
    private String rootXMLPath;// 测试用上传文件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        customerListener = new CustomIotSSEMsgListener(this);
        iotSSE = new IotSSEMsgLib(getApplicationContext(),customerListener);

        Button btnConnect = findViewById(R.id.button_connect);
        btnConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//FIXME: unique_device_id_for_test 全网唯一设备id;
                // 手机上建议采用IMEI码，保证全球唯一且不变化，另外uid可以传空，推送服务器以did&uid作为后台唯一的设备推送地址;
                // 如果没有帐号体系，uid 建议传空;
                // 如果uid不传空的话，代表sendMessage()方法的调用方只有在相同的uid下，才能够did进行消息发送，否则接受方无法收到;
                // 即A设备APP上调用：connectSSE("unique_device_id_for_test_source_A","user-id");
                // 即B设备APP上调用：connectSSE("unique_device_id_for_test_dest_B","user-id")；
                // A和B设备都注册到了user-id上，此时A和B设备调用sendMessage才能够成功接收消息。
                boolean isConnect = iotSSE.connectSSE("unique_device_id_for_test","");
                boolean isSSEConnected = iotSSE.isSSEConnected();
                TextView txtView = findViewById(R.id.txt_contet);
                txtView.setText("call connectSSE  = " + isConnect + " and is ready = " + isSSEConnected);
            }
        });

        Button btnSend = findViewById(R.id.button_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //FIXME: serial_number_id 可以是自增id，用于做消息追踪
                // 通过外网的方式，发送消息给unique_device_id_for_test设备
                // 此处模拟发送消息给到自己APP的unique_device_id_for_test
                iotSSE.sendMessage("unique_device_id_for_test","serial_number_id","hello","hello,i am sender!");
            }
        });

        Button btnReconnect = findViewById(R.id.button_reconnect);
        btnReconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 重新连接到后台，断开一切长连接并进行重连
                iotSSE.reConnectSSE("unique_device_id_for_test","");
            }
        });

        rootXMLPath = getApplicationContext().getCacheDir().getPath();
        final File bosFile = writeToXML("newfiletest","hello world");

        Button btnSendFile = findViewById(R.id.button_sendfile);
        btnSendFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
            // 上传文件到目标地址
            // syncFileToCloud的参数1是要发送的目标id，文件上传到云服务成功后会回调onSendFileToCloud，
            // 参数3是非常重要的参数，一般传的是connectSSE（）接口中传递的第一个参数，后台用该参数标识文件来源.
                iotSSE.syncFileToCloud("unique_device_id_for_test-dest",bosFile,"unique_device_id_for_test");
            }
        });
    }

    private boolean isSSEConnected(){
        if(null != iotSSE){
            return  iotSSE.isSSEConnected();
        }
        return false;
    }
    /**
     * 创建文件夹
     *
     * @param fileDirectory
     */
    public void createDirectory(String fileDirectory) {
        File file = new File(fileDirectory);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    /**
     * 保存内容到TXT文件中
     *
     * @param fileName
     * @param content
     * @return
     */
    public File writeToXML(String fileName, String content) {
        FileOutputStream fileOutputStream;
        BufferedWriter bufferedWriter;
        createDirectory(rootXMLPath);
        File file = new File(rootXMLPath + "/" + fileName + ".txt");
        try {
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(content);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return file;
    }
}
