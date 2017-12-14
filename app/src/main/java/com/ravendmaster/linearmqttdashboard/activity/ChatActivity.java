package com.ravendmaster.linearmqttdashboard.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.google.gson.Gson;
import com.ravendmaster.linearmqttdashboard.adapter.MsgAdapter;
import com.ravendmaster.linearmqttdashboard.adapter.MsgAdapterPolice;
import com.ravendmaster.linearmqttdashboard.bean.AnswerJsonChat;
import com.ravendmaster.linearmqttdashboard.bean.JsonDataAmq;

import com.ravendmaster.linearmqttdashboard.bean.PassWord;
import com.ravendmaster.linearmqttdashboard.bean.Person;
import com.ravendmaster.linearmqttdashboard.utils.MsgSendUtils;
import com.ravendmaster.linearmqttdashboard.utils.MsgType;
import com.sdses.mqtthead.R;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android_serialport_api.ReceiveData;


public class ChatActivity extends AppCompatActivity implements View.OnTouchListener, OnItemClickListener {

    private static final String TAG = "sdses_ChatActivity";

    public static MsgAdapterPolice adapter;

    public static ListView lv;

    EditText etQustion;

    static EditText etName = null;

    public static AlertView alertView = null;

    private InputMethodManager imm;

    public static boolean dismiss = true;

    public static List<Integer> actions;

    public static List<String> sings;

    static {

        actions = new ArrayList<Integer>();
        sings = new ArrayList<String>();

        actions.add(10224); //迈克尔杰克逊动作
        actions.add(6);  //小苹果动作
        actions.add(1006);  //最炫民族风动作
        actions.add(10225); //中国武术动作

        sings.add("ai/ai05res/a1/res/audio/dance/mj.wav");
        sings.add("ai/ai05res/a1/res/audio/dance/xpg.mp3");
        sings.add("ai/ai05res/a1/res/audio/dance/zxmzf.wav");
        sings.add("ai/ai05res/a1/res/audio/dance/dzzl_u05_019_1.wav"); // TODO 机器人不一样，地址不一样

    }

    public static void cleanPersonData(){
        Person person = Person.getPersonInstance();
        person = null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //全屏
        hideBottomUIMenu();
        setContentView(R.layout.chat_layout);
//        ChatActivity.msgList.clear(); //清空已有消息
        lv = (ListView) findViewById(R.id.lv_chat);
        etQustion = (EditText) findViewById(R.id.input_text);
//        etQustion.setOnTouchListener(this);  // 输入框监听
//        AmqService.startServiceAction(this); //启动AMQ管理的Service
        initMsgs();

        adapter = new MsgAdapterPolice(ChatActivity.this, R.layout.msg_item, MainActivityHello.msgList);
        lv.setAdapter(adapter);

        lv.setSelection(MainActivityHello.msgList.size());

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                
            }
        });

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        alertView = new AlertView("密码确认", "请输入密码", "取消", null, new String[]{"完成"}, this, AlertView.Style.Alert, this);
        ViewGroup extView = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.dialog_pass, null);
        etName = (EditText) extView.findViewById(R.id.input_text);
        etName.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        // 监听焦点
        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                Log.d(TAG, "onFocusChange: 密码焦点状态： " + focus);
                ChatActivity.dismiss = !focus;
            }
        });
        alertView.addExtView(extView);

        Log.d(TAG, "onCreate: 开启任务");
        MainActivityHello.handler.postDelayed(task, 1000);//第一次调用,延迟1秒执行task
    }

    private void initMsgs() {
        int size = MainActivityHello.msgList.size();
        if (size == 0) {
            JsonDataAmq msg1 = new JsonDataAmq(JsonDataAmq.TYPE_SPEAK, "你好小神！");
            MainActivityHello.msgList.add(msg1);
            JsonDataAmq msg2 = new JsonDataAmq(JsonDataAmq.TYPE_ROBOT, "您好，有什么可以帮助您的？");
            MainActivityHello.msgList.add(msg2);
            return;
        }
        JsonDataAmq msg = MainActivityHello.msgList.get(size - 1);
        String msgData = msg.getmsgData();
        if (TextUtils.isEmpty(msgData) || msgData.equals("您好，有什么可以帮助您的？")) {
            return;
        }
        JsonDataAmq msg1 = new JsonDataAmq(JsonDataAmq.TYPE_SPEAK, "你好小神！");
        MainActivityHello.msgList.add(msg1);
        JsonDataAmq msg2 = new JsonDataAmq(JsonDataAmq.TYPE_ROBOT, "您好，有什么可以帮助您的？");
        MainActivityHello.msgList.add(msg2);
    }

    // 定时任务
    private Runnable task = new Runnable() {
        @Override
        public void run() {
            /**
             * 此处执行任务
             * */
            MainActivityHello.handler.sendEmptyMessage(9);
            MainActivityHello.handler.postDelayed(this, 5 * 1000);//延迟5秒,再次执行task本身,实现了循环的效果
        }
    };

    /**
     * 对字符串进行Base64编码
     *
     * @param bstr
     * @return
     */
    public static String encode(byte[] bstr) {
        return new String(Base64.encode(bstr, Base64.DEFAULT));
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //只有当手指在rightDrawable区域时，才return true 表示已经处理事件，其它return false表示由框架处理。
        if (event.getRawX() >= (etQustion.getRight() - etQustion.getCompoundDrawables()[2].getBounds().width())) {
            System.out.println(event);//打印事件
            //手指在rightDrawable区域时并且是 MotionEvent.ACTION_UP时才去实现点击rightDrawable事件代码。
            //MotionEvent.ACTION_DOWN,MotionEvent.ACTION_MOVE 跳过。
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // your action here
                etQustion.requestFocus();//如果此时search没有焦点，要申请下。
                showEmotion();//你的方法
            }
            return true;//表示事件在这里已经处理，不会继续传。
        }
        return false;//表示由框架处理，那么会有edit的行为，如粘贴等。
    }

    private void showEmotion() {

        final String inputText = etQustion.getText().toString();
        etQustion.setText("");
        if (TextUtils.isEmpty(inputText)) {
//            AmqService.toastAmq("请输入问题！");  // TODO 请输入问题
        } else {
            // TODO 发送HTTP请求
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // question的Base64编码
                    String encodedString = Base64.encodeToString(inputText.getBytes(), Base64.DEFAULT);
//                    AmqService.toastAmq(encodedString);
//                    AmqService.logAmq("编码后的问题： " + encodedString); // TODO 编码后的问题
                    //创建okHttpClient对象
                    OkHttpClient mOkHttpClient = new OkHttpClient();

                    RequestBody formBody = new FormEncodingBuilder()
                            .add("question", encodedString)
                            .add("mac", "b0f1ec6ba796")
                            .build();

                    //创建一个Request
                    final Request request = new Request.Builder()
                            .url("http://robots.sdses.com:9088/BankQA/answerTOYL.action?")
                            .post(formBody)
                            .build();
                    //new call
                    Call call = mOkHttpClient.newCall(request);
                    //请求加入调度
                    call.enqueue(new Callback() {
                        @Override
                        public void onFailure(Request request, IOException e) {
//                            AmqService.toastAmq("网络连接失败！");  // TODO 网络连接失败
                        }

                        @Override
                        public void onResponse(final Response response) throws IOException {
                            String htmlStr = response.body().string();
                            Log.d("main", htmlStr + "\n");
                            Gson gson = new Gson();
                            AnswerJsonChat answerJson = gson.fromJson(htmlStr, AnswerJsonChat.class);
                            String answer = answerJson.getQuesAnsw().getAnswer();
                            String question = answerJson.getQuesAnsw().getQuestion();
                            Log.d("main", answer);
                            Message message = MainActivityHello.handler.obtainMessage();
                            message.obj = question + "#" + answer;
                            message.what = 1;
                            MainActivityHello.handler.sendMessage(message);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onItemClick(Object o, int position) {
        // 点击了确认按钮
        if (o == alertView && position != AlertView.CANCELPOSITION) {
            String name = etName.getText().toString();
            if (TextUtils.isEmpty(name)) {
//                Toast.makeText(this, "密码为空", Toast.LENGTH_SHORT).show();
//                MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "密码为空");
                MsgSendUtils.sendStringMsg(MsgType.ACTIVE, "唤醒机器人");
                alertView.dismiss(); //取消显示
            } else {
                String password = etName.getText().toString().trim();
                if (password.equals(PassWord.getPassWord().getPw())) {
//                    MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "密码为空");
                    String[] splites = PassWord.getPassWord().getAction().split("#");
                    //socket发送指令
                    int action = Integer.parseInt(splites[2]);
                    int index = actions.indexOf(action);
                    String sing = "";
                    alertView.dismiss(); //取消显示
                    MsgSendUtils.sendStringMsg(MsgType.NO_ACTIVE, "休眠机器人");
                    try {
                        TimeUnit.SECONDS.sleep(1); // 休眠一秒钟
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.d(TAG, "onItemClick: 出现异常");
                    }
                    Log.d(TAG, "onItemClick: 发送了跳舞指令");
                    if (index != -1) {
                        sing = sings.get(index);
                        MsgSendUtils.sendStringMsg(MsgType.PLAY_AUDIO, sing);
                    }
                    MsgSendUtils.sendStringMsg(MsgType.PLAY_ACTION, action + "");
                    // 回到欢迎界面
                    Intent intentMian = new Intent(this, MainActivityHello.class);
                    intentMian.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    this.startActivity(intentMian); // 启动欢迎界面
                } else {
                    MsgSendUtils.sendStringMsg(MsgType.PLAY_TTS, "对不起，密码输入错误");
                    alertView.dismiss(); //取消显示
//                    sendSocket(8401, "密码错误");
                }
            }
            return;
        }else {
            MsgSendUtils.sendStringMsg(MsgType.ACTIVE, "唤醒机器人");
        }
        alertView.dismiss(); //取消显示
        // 点击了取消按钮
//        Toast.makeText(this, "取消操作", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG, "onRestart: 重新开始了任务");
        MainActivityHello.handler.postDelayed(task, 1000);//第一次调用,延迟1秒执行task
        hideBottomUIMenu();
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideBottomUIMenu();
    }

    protected void onPause() {
        super.onPause();
        hideBottomUIMenu();
    }

    @Override
    protected void onStop() {
        super.onStop();
        hideBottomUIMenu();
        Log.d(TAG, "onStop: 取消任务");
        MainActivityHello.handler.removeCallbacks(task); // 取消任务
    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
