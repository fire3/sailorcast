package com.crixmod.sailorcast.view;


import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.cyberplayer.dlna.DLNADeviceEventListener;
import com.baidu.cyberplayer.dlna.DLNAEventType;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.IDisableDLNACallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.IEnableDLNACallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.IGetMuteCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.IGetSupportedProtocolsCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.IGetVolumeCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.IPauseCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.IPlayCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.ISeekCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.ISelectRendererDeviceCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.ISetMediaMetaDataCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.ISetMediaURICallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.ISetMuteCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.ISetVolumeCallBack;
import com.baidu.cyberplayer.dlna.IDLNAServiceProvider.IStopCallBack;
import com.crixmod.sailorcast.R;

public class DLNAControlActivity extends Activity implements OnClickListener,IStopCallBack,IEnableDLNACallBack,IDisableDLNACallBack,
        IGetVolumeCallBack,ISetVolumeCallBack,IPauseCallBack,ISeekCallBack,ISetMuteCallBack,IPlayCallBack,ISetMediaURICallBack,
        ISelectRendererDeviceCallBack,IGetSupportedProtocolsCallBack,IGetMuteCallBack,ISetMediaMetaDataCallBack {

	private final int SELECT_SUC = 103;
	private final int SELECT_FAIL = 104;
	private final int SET_URI_SUC = 105;
	private final int SET_URI_FAIL = 106;
	private final int PLAY_SUC = 107;
	private final int PLAY_FAIL = 108;
	private final int PAUSE_SUC = 109;
	private final int PAUSE_FAIL = 110;
	private final int STOPPED = 113;
	private final int INIT_FAIL = 114;
	private final int MUTE_STAT = 115;
	private final int MUTE_CANCEL = 116;
	private final int INIT_SUC = 117;

	private final int UPDATE_DURATION = 201;
	private final int UPDATE_CURRENT = 202;
	private final int UPDATE_VOLUME = 203;

	private boolean playing = false;
	boolean isMute = false;
	private int mVolume = -1;

	private Button backButton;
	private ImageButton playButton;
	private TextView deviceName;
	private ImageButton muteButton;
	private SeekBar progressSeekBar;
	private SeekBar volumeSeekBar;
	private TextView timeCurrent;
	private TextView timeDuration;
	private WakeLock wakeLock = null;
	private ProgressDialog alertPorgress;

	public static IDLNAServiceProvider serviceProvider = null;
	private String selectedDevice;
	private String playUrl = null;

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SELECT_SUC:
				String state = serviceProvider.getRenderState();
				eventListener.onEventReceived(
						DLNAEventType.RENDER_STATE_UPDATE, state);
				Toast.makeText(DLNAControlActivity.this,
                        "Linked, Setting URL ", Toast.LENGTH_SHORT).show();
				serviceProvider.getVolume(DLNAControlActivity.this);
				serviceProvider.stop(DLNAControlActivity.this);
				serviceProvider.setMediaURI(playUrl,DLNAControlActivity.this);
				break;
			case SET_URI_SUC:
				if (alertPorgress != null && alertPorgress.isShowing())
					alertPorgress.dismiss();
				Toast.makeText(DLNAControlActivity.this, "Set URL success",
                        Toast.LENGTH_SHORT).show();
				playButton.setEnabled(false);
				serviceProvider.play(DLNAControlActivity.this);
				break;
			case SET_URI_FAIL:
				if (alertPorgress != null && alertPorgress.isShowing())
					alertPorgress.dismiss();
				Toast.makeText(DLNAControlActivity.this, "Set URL fail",
                        Toast.LENGTH_SHORT).show();
				break;
			case PLAY_SUC:
				playing = true;
				playButton.setImageResource(R.drawable.pause_btn);
				playButton.setEnabled(true);
				break;
			case PLAY_FAIL:
				playing = false;
				playButton.setImageResource(R.drawable.play_btn);
				playButton.setEnabled(true);
				break;
			case PAUSE_SUC:
				playing = false;
				playButton.setImageResource(R.drawable.play_btn);
				playButton.setEnabled(true);
				break;
			case PAUSE_FAIL:
				playing = true;
				playButton.setImageResource(R.drawable.pause_btn);
				playButton.setEnabled(true);
				break;
			case STOPPED:
				playButton.setEnabled(true);
				playButton.setImageResource(R.drawable.play_btn);
				break;
			case INIT_FAIL:
				break;
			case UPDATE_DURATION:
				timeDuration.setText(msg.obj.toString());
				if (!timeDuration.equals("00:00:00")) {
					progressSeekBar.setMax(caculateTime(msg.obj.toString()));
					progressSeekBar.setEnabled(true);
				}
				break;
			case UPDATE_CURRENT:
				progressSeekBar.setProgress(caculateTime(msg.obj.toString()));
				break;
			case UPDATE_VOLUME:
				volumeSeekBar.setProgress((int) (msg.arg1 / 10));
				break;
			case MUTE_STAT:
				isMute = true;
				muteButton.setEnabled(true);
				volumeSeekBar.setEnabled(true);
				muteButton.setImageResource(R.drawable.volume_btn_mute);
				break;
			case MUTE_CANCEL:
				isMute = false;
				muteButton.setEnabled(true);
				volumeSeekBar.setEnabled(true);
				muteButton.setImageResource(R.drawable.volume_btn);
				break;
			default:
				break;
			}
		}
	};

//	private DLNAActionListener actionListener = new DLNAActionListener() {
//
//		@Override
//		public void onStop(boolean isSucess, int errCode, String errDesc) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onSetVolume(boolean isSucess, int errCode, String errDesc) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onSetMute(boolean isSucess, int errCode, String errDesc) {
//			// TODO Auto-generated method stubd
//			if (isSucess == isMute) {
//				uiHandler.sendEmptyMessage(MUTE_STAT);
//			} else {
//				uiHandler.sendEmptyMessage(MUTE_CANCEL);
//			}
//
//		}
//
//		@Override
//		public void onSetMediaURI(boolean isSucess, int errCode, String errDesc) {
//			// TODO Auto-generated method stub
//			if (isSucess)
//				uiHandler.sendEmptyMessage(SET_URI_SUC);
//			else
//				uiHandler.sendEmptyMessage(SET_URI_FAIL);
//		}
//
//		@Override
//		public void onSelectRenderDevice(boolean isSuccess, int errCode,
//				String errDesc) {
//			// TODO Auto-generated method stub
//			if (isSuccess)
//				uiHandler.sendEmptyMessage(SELECT_SUC);
//			else {
//				if (errDesc == null)
//					errDesc = "";
//				Toast.makeText(DLNAControlActivity.this,
//						"Select Device Fail " + errCode + ": " + errDesc,
//						Toast.LENGTH_LONG).show();
//			}
//		}
//
//		@Override
//		public void onSeek(boolean isSucess, int errCode, String errDesc) {
//			// TODO Auto-generated method stub
//			if (isSucess)
//				Toast.makeText(DLNAControlActivity.this, "Seek Complete ",
//						Toast.LENGTH_LONG).show();
//			else {
//				if (errDesc == null)
//					errDesc = "";
//				Toast.makeText(DLNAControlActivity.this,
//						"Seek Fail " + errCode + ": " + errDesc,
//						Toast.LENGTH_LONG).show();
//			}
//		}
//
//		@Override
//		public void onPlay(boolean isSucess, int errCode, String errDesc) {
//			// TODO Auto-generated method stub
//			if (isSucess)
//				uiHandler.sendEmptyMessage(PLAY_SUC);
//			else {
//				uiHandler.sendEmptyMessage(PLAY_FAIL);
//				if (errDesc == null)
//					errDesc = "";
//				Toast.makeText(DLNAControlActivity.this,
//						"Play Fail " + errCode + ": " + errDesc,
//						Toast.LENGTH_LONG).show();
//			}
//		}
//
//		@Override
//		public void onPause(boolean isSucess, int errCode, String errDesc) {
//			// TODO Auto-generated method stub
//			if (isSucess)
//				uiHandler.sendEmptyMessage(PAUSE_SUC);
//			else {
//				uiHandler.sendEmptyMessage(PAUSE_FAIL);
//				if (errDesc == null)
//					errDesc = "";
//				Toast.makeText(DLNAControlActivity.this,
//						"Pause Fail " + errCode + ": " + errDesc,
//						Toast.LENGTH_LONG).show();
//			}
//		}
//
//		@Override
//		public void onGetVolume(boolean isSucess, int result, int errCode,
//				String errDesc) {
//			// TODO Auto-generated method stub
//			if (isSucess) {
//				Message msg = new Message();
//				msg.what = UPDATE_VOLUME;
//				msg.arg1 = result;
//				uiHandler.sendMessage(msg);
//			}
//		}
//
//		@Override
//		public void onGetSupportedProtocols(boolean isSucess, String result,
//				int errCode, String errDesc) {
//			// TODO Auto-generated method stub
//
//		}
//
//		@Override
//		public void onGetMute(boolean isSucess, boolean result, int errCode,
//				String errDesc) {
//			// TODO Auto-generated method stub
//			if (isSucess == result)
//				uiHandler.sendEmptyMessage(MUTE_STAT);
//			else {
//				uiHandler.sendEmptyMessage(MUTE_CANCEL);
//			}
//		}
//
//		@Override
//		public void onEnableDLNA(boolean isSuccess, int errCode, String errDesc) {
//			// TODO Auto-generated method stub
//			if (isSuccess)
//				uiHandler.sendEmptyMessage(MUTE_STAT);
//			else {
//				uiHandler.sendEmptyMessage(MUTE_CANCEL);
//			}
//		}
//
//        @Override
//		public void onDisableDLNA(boolean isSuccess, int errCode, String errDesc) {
//			// TODO Auto-generated method stub
//
//		}
//
//        @Override
//        public void onSetMediaMetaData(boolean isSucess, int errCode, String errDesc){
//
//        }
//
//	};

	private DLNADeviceEventListener eventListener = new DLNADeviceEventListener() {

		@Override
		public void onEventReceived(DLNAEventType eventType, String eventMsg) {
			// TODO Auto-generated method stub
			if (eventType == DLNAEventType.RENDER_STATE_UPDATE) {
				if (eventMsg.equalsIgnoreCase("playing")) {
					uiHandler.sendEmptyMessage(PLAY_SUC);
				} else if (eventMsg.equalsIgnoreCase("paused_playback")) {
					uiHandler.sendEmptyMessage(PAUSE_SUC);

				} else if (eventMsg.equalsIgnoreCase("stopped")) {
					uiHandler.sendEmptyMessage(STOPPED);
				} else if (eventMsg.equalsIgnoreCase("transitioning")) {
				}

			}

			else if (eventType == DLNAEventType.DURATION_UPDATE) {
				if (eventMsg != null) {
					Message msg = new Message();
					msg.what = UPDATE_DURATION;
					msg.obj = eventMsg;
					uiHandler.sendMessage(msg);
				}
			} else if (eventType == DLNAEventType.POSITION_UPDATE) {
				if (eventMsg != null) {
					Message msg = new Message();
					msg.what = UPDATE_CURRENT;
					msg.obj = eventMsg;
					uiHandler.sendMessage(msg);
				}
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// init BCyberPlayerFactory first
		setContentView(R.layout.dlna_control);

		serviceProvider.addEventListener(eventListener);
//		serviceProvider.addActionCallBack(actionListener);
		playUrl = getIntent().getStringExtra("url");
		selectedDevice = getIntent().getStringExtra("device");
		initUI();
		initDevice();
		PowerManager powerManager = (PowerManager) this
				.getSystemService(Activity.POWER_SERVICE);
		this.wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK,
				"My Lock");
	}

	private void initUI() {
		backButton = (Button) findViewById(R.id.back);
		backButton.setOnClickListener(this);
		playButton = (ImageButton) findViewById(R.id.pause);
		playButton.setOnClickListener(this);
		deviceName = (TextView) findViewById(R.id.device_name);
		if (selectedDevice != null)
			deviceName.setText(selectedDevice);
		muteButton = (ImageButton) findViewById(R.id.volume);
		muteButton.setOnClickListener(this);
		progressSeekBar = (SeekBar) findViewById(R.id.mediacontroller_progress);
		progressSeekBar
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub
						serviceProvider.seek(formatTime(seekBar.getProgress()),DLNAControlActivity.this);
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onProgressChanged(SeekBar seekBar,
							int progress, boolean fromUser) {
						// TODO Auto-generated method stub
						timeCurrent.setText(formatTime(progress));
					}
				});
		progressSeekBar.setEnabled(false);
		volumeSeekBar = (SeekBar) findViewById(R.id.volume_progress);
		volumeSeekBar.setMax(10);
		volumeSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				int volume = 10 * seekBar.getProgress();
				if (volume != 0) {
					muteButton.setImageResource(R.drawable.volume_btn);
					isMute = false;
				}
				if (mVolume == volume) {
					return;
				} else {
					serviceProvider.setVolume(volume,DLNAControlActivity.this);
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub

			}
		});
		timeCurrent = (TextView) findViewById(R.id.time_current);
		timeDuration = (TextView) findViewById(R.id.time);
	}

	private void initDevice() {
		alertPorgress = new ProgressDialog(this);
		alertPorgress.setTitle("Initializing");
		alertPorgress.show();
		serviceProvider.selectRenderDevice(selectedDevice,this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		switch (keyCode) {
		case KeyEvent.KEYCODE_VOLUME_UP:
		case KeyEvent.KEYCODE_VOLUME_DOWN:
			break;
		case KeyEvent.KEYCODE_BACK:
			finish();
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.wakeLock.acquire();
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.wakeLock.release();
	}

	private String formatTime(int second) {
		int hh = second / 3600;
		int mm = second % 3600 / 60;
		int ss = second % 60;
		String strTemp = null;
		strTemp = String.format("%02d:%02d:%02d", hh, mm, ss);
		return strTemp;
	}

	private int caculateTime(String time) {
		if (time.length() > 8 && time.lastIndexOf(":") < time.length() - 2)
			time = time.substring(0, time.lastIndexOf(":") + 3);
		int a = 0;
		try {
			String second = time.substring(time.lastIndexOf(":") + 1);
			if (second.length() > 2)
				second = second.substring(0, 1);
			a = Integer.parseInt(second);
			time = time.substring(0, time.lastIndexOf(":"));
			String minute = time.substring(time.lastIndexOf(":") + 1);
			a = a + 60 * Integer.parseInt(minute);
			time = time.substring(0, time.lastIndexOf(":"));
			a = a + 3600 * Integer.parseInt(time);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return a;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id = v.getId();
		// play or pause
		if (id == R.id.pause && serviceProvider != null) {
			playButton.setEnabled(false);
			if (playing) {
				serviceProvider.pause(this);
			} else {
				serviceProvider.play(this);
			}
		}

		// the last show

		else if (id == R.id.volume) {
			muteButton.setEnabled(false);
			volumeSeekBar.setEnabled(false);
			isMute = !isMute;
			serviceProvider.setMuteStat(isMute,this);
		} else if (id == R.id.back) {
			finish();
		}
	}
    @Override
    public void onStop(boolean isSucess, int errCode, String errDesc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSetVolume(boolean isSucess, int errCode, String errDesc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSetMute(boolean isSucess, int errCode, String errDesc) {
        // TODO Auto-generated method stubd
        if (isSucess == isMute) {
            uiHandler.sendEmptyMessage(MUTE_STAT);
        } else {
            uiHandler.sendEmptyMessage(MUTE_CANCEL);
        }

    }

    @Override
    public void onSetMediaURI(boolean isSucess, int errCode, String errDesc) {
        // TODO Auto-generated method stub
        if (isSucess)
            uiHandler.sendEmptyMessage(SET_URI_SUC);
        else
            uiHandler.sendEmptyMessage(SET_URI_FAIL);
    }

    @Override
    public void onSelectRenderDevice(boolean isSuccess, int errCode,
            String errDesc) {
        // TODO Auto-generated method stub
        if (isSuccess)
            uiHandler.sendEmptyMessage(SELECT_SUC);
        else {
            if (errDesc == null)
                errDesc = "";
            Toast.makeText(DLNAControlActivity.this,
                    "Select Device Fail " + errCode + ": " + errDesc,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onSeek(boolean isSucess, int errCode, String errDesc) {
        // TODO Auto-generated method stub
        if (isSucess)
            Toast.makeText(DLNAControlActivity.this, "Seek Complete ",
                    Toast.LENGTH_LONG).show();
        else {
            if (errDesc == null)
                errDesc = "";
            Toast.makeText(DLNAControlActivity.this,
                    "Seek Fail " + errCode + ": " + errDesc,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPlay(boolean isSucess, int errCode, String errDesc) {
        // TODO Auto-generated method stub
        if (isSucess)
            uiHandler.sendEmptyMessage(PLAY_SUC);
        else {
            uiHandler.sendEmptyMessage(PLAY_FAIL);
            if (errDesc == null)
                errDesc = "";
            Toast.makeText(DLNAControlActivity.this,
                    "Play Fail " + errCode + ": " + errDesc,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPause(boolean isSucess, int errCode, String errDesc) {
        // TODO Auto-generated method stub
        if (isSucess)
            uiHandler.sendEmptyMessage(PAUSE_SUC);
        else {
            uiHandler.sendEmptyMessage(PAUSE_FAIL);
            if (errDesc == null)
                errDesc = "";
            Toast.makeText(DLNAControlActivity.this,
                    "Pause Fail " + errCode + ": " + errDesc,
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onGetVolume(boolean isSucess, int result, int errCode,
            String errDesc) {
        // TODO Auto-generated method stub
        if (isSucess) {
            Message msg = new Message();
            msg.what = UPDATE_VOLUME;
            msg.arg1 = result;
            uiHandler.sendMessage(msg);
        }
    }

    @Override
    public void onGetSupportedProtocols(boolean isSucess, String result,
            int errCode, String errDesc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onGetMute(boolean isSucess, boolean result, int errCode,
            String errDesc) {
        // TODO Auto-generated method stub
        if (isSucess == result)
            uiHandler.sendEmptyMessage(MUTE_STAT);
        else {
            uiHandler.sendEmptyMessage(MUTE_CANCEL);
        }
    }

    @Override
    public void onEnableDLNA(boolean isSuccess, int errCode, String errDesc) {
        // TODO Auto-generated method stub
        if (isSuccess)
            uiHandler.sendEmptyMessage(MUTE_STAT);
        else {
            uiHandler.sendEmptyMessage(MUTE_CANCEL);
        }
    }

    @Override
    public void onDisableDLNA(boolean isSuccess, int errCode, String errDesc) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onSetMediaMetaData(boolean isSucess, int errCode, String errDesc){

    }
}
