package com.getui.getuiflut;

import android.content.Context;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.message.BindAliasCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTNotificationMessage;
import com.igexin.sdk.message.GTTransmitMessage;

import java.util.HashMap;
import java.util.Map;

public class FlutterIntentService extends GTIntentService {

    @Override
    public void onReceiveServicePid(Context context, int pid) {
        Log.d(TAG, "onReceiveServicePid -> " + pid);

    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);
        GetuiflutPlugin.transmitMessageReceive(clientid, "onReceiveClientId");
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        Log.d(TAG, "onReceiveMessageData -> " + "appid = " + msg.getAppid() + "\ntaskid = " + msg.getTaskId() + "\nmessageid = " + msg.getMessageId() + "\npkg = " + msg.getPkgName()
                + "\ncid = " + msg.getClientId() + "\npayload:" + new String(msg.getPayload()));
        Map<String, Object> payload = new HashMap<>();
        payload.put("messageId", msg.getMessageId());
        payload.put("payload", new String(msg.getPayload()));
        payload.put("payloadId", msg.getPayloadId());
        payload.put("taskId", msg.getTaskId());
        GetuiflutPlugin.transmitMessageReceive(payload, "onReceiveMessageData");
    }

    @Override
    public void onReceiveOnlineState(Context context, boolean b) {
        GetuiflutPlugin.transmitMessageReceive(String.valueOf(b), "onReceiveOnlineState");
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage gtCmdMessage) {
        int action = gtCmdMessage.getAction();

        if (action == PushConsts.BIND_ALIAS_RESULT || action == PushConsts.UNBIND_ALIAS_RESULT) {
            String sn = ((BindAliasCmdMessage) gtCmdMessage).getSn();
            String code = ((BindAliasCmdMessage) gtCmdMessage).getCode();
            /*  code 结果说明
                0：成功
                10099：SDK 未初始化成功
                30001：绑定别名失败，频率过快，两次调用的间隔需大于 1s
                30002：绑定别名失败，参数错误
                30003：当前 cid 绑定别名次数超限
                30004：绑定别名失败，未知异常
                30005：绑定别名时，cid 未获取到
                30006：绑定别名时，发生网络错误
                30007：别名无效
                30008：sn 无效 */


            
            Map<String, Object> result = new HashMap<>();
            result.put("action", action == PushConsts.BIND_ALIAS_RESULT ? "bindAlias" : "unbindAlias");
            result.put("sn", sn);
            result.put("result", code.equals("0"));
            result.put("error", "error code:" + code);
            GetuiflutPlugin.transmitMessageReceive(result, "onAliasResult");
            Log.d(TAG, "bind alias result sn = " + sn + ", code = " + code);
        } else{
            GetuiflutPlugin.transmitMessageReceive(gtCmdMessage.toString(), "onReceiveCommandResult");
        }
        
    }

    @Override
    public void onNotificationMessageArrived(Context context, GTNotificationMessage message) {
        Log.d(TAG, "onNotificationMessageArrived -> " + "appid = " + message.getAppid() + "\ntaskid = " + message.getTaskId() + "\nmessageid = "
                + message.getMessageId() + "\npkg = " + message.getPkgName() + "\ncid = " + message.getClientId() + "\ntitle = "
                + message.getTitle() + "\ncontent = " + message.getContent());
        Map<String, Object> notification = new HashMap<String, Object>();
        notification.put("messageId",message.getMessageId());
        notification.put("taskId",message.getTaskId());
        notification.put("title",message.getTitle());
        notification.put("content",message.getContent());
        GetuiflutPlugin.transmitMessageReceive(notification, "onNotificationMessageArrived");
    }

    @Override
    public void onNotificationMessageClicked(Context context, GTNotificationMessage message) {
        Log.d(TAG, "onNotificationMessageClicked -> " + "appid = " + message.getAppid() + "\ntaskid = " + message.getTaskId() + "\nmessageid = "
                + message.getMessageId() + "\npkg = " + message.getPkgName() + "\ncid = " + message.getClientId() + "\ntitle = "
                + message.getTitle() + "\ncontent = " + message.getContent());
        Map<String, Object> notification = new HashMap<String, Object>();
        notification.put("messageId",message.getMessageId());
        notification.put("taskId",message.getTaskId());
        notification.put("title",message.getTitle());
        notification.put("content",message.getContent());
        GetuiflutPlugin.transmitMessageReceive(notification, "onNotificationMessageClicked");
    }
}
