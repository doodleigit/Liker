package com.doodle.Home.service;

import android.util.Log;

import com.doodle.App;
import com.doodle.Home.model.Headers;
import com.doodle.utils.AppConstants;
import com.doodle.utils.PrefManager;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.Map;


import io.socket.client.Ack;
import io.socket.client.IO;
import io.socket.client.Manager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import io.socket.engineio.client.Transport;


public class SocketIOManager {
    private static final String TAG = SocketIOManager.class.getSimpleName();

    private static SocketIOManager mInstance;
    //Added @null
    public static Socket mSocket = null, wSocket = null;
    private PrefManager manager;
    private String token, deviceId, userId, socketId;

    public Socket getMSocketInstance() {
        manager = new PrefManager(App.getInstance());
        // To ensure the SAME instance of socket can be called from any activity/ fragment in the app
        //IF this is not done as is, the methods will not be attached properly to the socket

        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        token = manager.getToken();
        if (mSocket == null) {
            try {
                mSocket = IO.socket(AppConstants.SOCKET_MESSAGE);
                initializeMessageSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return mSocket;
    }

    public Socket getWSocketInstance() {
        manager = new PrefManager(App.getInstance());
        // To ensure the SAME instance of socket can be called from any activity/ fragment in the app
        //IF this is not done as is, the methods will not be attached properly to the socket

        deviceId = manager.getDeviceId();
        userId = manager.getProfileId();
        token = manager.getToken();
        if (wSocket == null) {
            try {
                wSocket = IO.socket(AppConstants.SOCKET_WEB);
                initializeWebSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        getMSocketInstance();
        return wSocket;
    }

    private void initializeWebSocket() {
        /**
         * In case you decide to add socket methods in different class, this can be ignored
         */
        wSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT connect");

                String id = wSocket.connect().id();
                App.setSocketId(id);
                Log.d(TAG, "call: " + id);
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("TAG", "Event disconnect, Socket is disconnected");
            }
        }).on("test", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        });

        wSocket.connect();
    }

    private void initializeMessageSocket() {
        /**
         * In case you decide to add socket methods in different class, this can be ignored
         */
        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT connect");

                String id = mSocket.connect().id();
                App.setmSocketId(id);
                Log.d(TAG, "call: " + id);
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("TAG", "Event disconnect, Socket is disconnected");
            }
        }).on("test", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        });

        mSocket.connect();
    }

//    private SocketIOManager() {
//        try {
//            mSocket = IO.socket(SOCKET_IO_SERVER);
//        } catch (URISyntaxException e) {
//            Log.e(TAG, "URL is not correct");
//        }
//    }
//
//    public synchronized static SocketIOManager getInstance() {
//        if (mInstance != null) {
//            return mInstance;
//        }
//
//        mInstance =  new SocketIOManager();
//        return mInstance;
//    }

    public void start() {
        Log.d(TAG, "start socket...");
        if (mSocket.connected()) {
            return;
        }

        mSocket.off();

        mSocket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.d(TAG, "EVENT connect");

                String id = mSocket.connect().id();
                Log.d(TAG, "call: " + id);
            }
        }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                if (args != null) {
                    Log.e(TAG, "Event error: " + args[0].toString());
                }
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                Log.e("TAG", "Event disconnect, Socket is disconnected");
            }
        });

        mSocket.connect();
    }


    public void stop() {
        Log.d(TAG, "stop socket...");

        if (mSocket != null) {
            mSocket.disconnect();
        }
    }
}