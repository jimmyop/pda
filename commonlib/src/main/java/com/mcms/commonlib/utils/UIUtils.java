package com.mcms.commonlib.utils;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnShowListener;
import android.graphics.drawable.AnimationDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mcms.commonlib.R;


/**
 * 图形工具类
 * 
 * @author xuanweiqing
 * 
 */
public class UIUtils {
	private static UIUtils instance = null;

	// 获取日期格式器对象
	private DateFormat fmtDateAndTime = DateFormat.getDateTimeInstance();

	// 获取一个日历对象
	private Calendar dateAndTime = Calendar.getInstance(Locale.CHINA);

	/**
	 * 图片上传监听
	 */
	private OnTimeControlListener onTimeControlListener = null;

	/**
	 * 多个时间控件做区分
	 */
	private int mStartTime = -1;

	/**
	 * 时间回调字符串
	 */
	private StringBuilder dateBuilder = null;

	private Dialog loadingDialog;

	public static synchronized UIUtils getInstance() {
		if (instance == null)
			instance = new UIUtils();
		return instance;
	}

	public static void startAnimaltion(View mLoadingAnimation) {
		Animation mAnimation = new RotateAnimation(0, 359,
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
				0.5f);
		LinearInterpolator lin = new LinearInterpolator();
		mAnimation.setRepeatCount(-1);
		mAnimation.setInterpolator(lin);
		mAnimation.setDuration(1000);
		mAnimation.setFillAfter(true);
		mLoadingAnimation.startAnimation(mAnimation);
	}

	/**
	 * 一个地方多个日期控件
	 * 
	 * @param activity
	 * @param startTime
	 */
	public void setDateControl(Activity activity, int startTime) {
		this.mStartTime = startTime;
		new MyDatePickerDialog(activity, timeSelection,
				dateAndTime.get(Calendar.YEAR),
				dateAndTime.get(Calendar.MONTH),
				dateAndTime.get(Calendar.DAY_OF_MONTH)).show();
	}

	class MyDatePickerDialog extends DatePickerDialog {

		public MyDatePickerDialog(Context context, OnDateSetListener callBack,
				int year, int monthOfYear, int dayOfMonth) {
			super(context, callBack, year, monthOfYear, dayOfMonth);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onStop() {
			// 高版本android必须把onStop去掉，不然会调用两次回调函数
			// super.onStop();
		}
	}

	// 当点击DatePickerDialog控件的设置按钮时，调用该方法
	private DatePickerDialog.OnDateSetListener timeSelection = new DatePickerDialog.OnDateSetListener() {
		@Override
		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			// 修改日历控件的年，月，日
			// 这里的year,monthOfYear,dayOfMonth的值与DatePickerDialog控件设置的最新值一致
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, monthOfYear);
			dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			dateBuilder = new StringBuilder();
			dateBuilder.append(dateAndTime.get(Calendar.YEAR)).append("-")
					.append(dateAndTime.get(Calendar.MONTH) + 1).append("-")
					.append(dateAndTime.get(Calendar.DAY_OF_MONTH));

			onTimeControlListener.onDateContent(dateBuilder, mStartTime);
		}
	};

	public interface OnTimeControlListener {
		void onDateContent(StringBuilder dataTime, int startTime);

		void onTimeContent(StringBuilder dataTime, int startTime);
	}

	public void setOnImageUploadRefreshListener(
			OnTimeControlListener timeControlListener) {
		onTimeControlListener = timeControlListener;
	}

	private TimePickerDialog.OnTimeSetListener timeSet = new TimePickerDialog.OnTimeSetListener() {

		// 同DatePickerDialog控件
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			dateAndTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
			dateAndTime.set(Calendar.MINUTE, minute);

			DecimalFormat df = new DecimalFormat("00");
			String minuteStr = df.format(dateAndTime.get(Calendar.MINUTE));
			dateBuilder.append(" ")
					.append(dateAndTime.get(Calendar.HOUR_OF_DAY)).append(":")
					.append(minuteStr);
			// dateBuilder.append(" ")
			// .append(dateAndTime.get(Calendar.HOUR_OF_DAY)).append(":")
			// .append(dateAndTime.get(Calendar.MINUTE));
			onTimeControlListener.onTimeContent(dateBuilder, mStartTime);
			mStartTime = -1;
			dateBuilder = null;
		}
	};

	/**
	 * 一个地方时间多个控件
	 * 
	 * @param activity
	 * @param startTime
	 */
	public void setTimeControl(Activity activity, int startTime) {
		this.mStartTime = startTime;
		new MyTimePickerDialog(activity, timeSet,
				dateAndTime.get(Calendar.HOUR_OF_DAY),
				dateAndTime.get(Calendar.MINUTE), true).show();
	}

	class MyTimePickerDialog extends TimePickerDialog {

		public MyTimePickerDialog(Context context, OnTimeSetListener callBack,
				int hourOfDay, int minute, boolean is24HourView) {
			super(context, callBack, hourOfDay, minute, is24HourView);
			// TODO Auto-generated constructor stub
		}

		@Override
		protected void onStop() {
			// 高版本android必须把onStop去掉，不然会调用两次回调函数
			// super.onStop();
		}
	}

	/**
	 * 显示弹出框
	 * 
	 * @param activity
	 * @param content
	 */
//	public void showProgressDialog(Activity activity, String content) {
//		LayoutInflater inflater = LayoutInflater.from(activity);
//		View v = inflater.inflate(R.layout.dialog_loading_new, null);// 得到加载view
//		TextView tipTextView = (TextView) v
//				.findViewById(R.id.dialog_loading_text);// 提示文字
//		final ImageView iconImageView = (ImageView) v
//				.findViewById(R.id.iv_dialog_loading);
//		tipTextView.setText(content);// 设置加载信息
//		loadingDialog = new Dialog(activity, R.style.loading_dialog);// 创建自定义样式dialog
//		loadingDialog.setOnShowListener(new OnShowListener() {
//			@Override
//			public void onShow(DialogInterface dialog) {
//				AnimationDrawable animationDrawable = (AnimationDrawable) iconImageView
//						.getBackground();
//				animationDrawable.start();
//			}
//		});
//		loadingDialog.setOnCancelListener(new OnCancelListener() {
//
//			@Override
//			public void onCancel(DialogInterface dialog) {
//				AnimationDrawable animationDrawable = (AnimationDrawable) iconImageView
//						.getBackground();
//				animationDrawable.stop();
//			}
//		});
//		loadingDialog.setCancelable(false);// 不可以用“返回键”取消
//		loadingDialog.setCanceledOnTouchOutside(false);
//		loadingDialog.setContentView(v, new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));// 设置布局
//		loadingDialog.show();
//	}

	/**
	 * 
	 * @param activity
	 */
	public void hideProgressDialog(Activity activity) {
		if (loadingDialog != null) {
			loadingDialog.dismiss();
		}
	}

	/**
	 * 获取Dialog
	 * 
	 * @return
	 */
	public Dialog getProgressDialog() {
		return loadingDialog;
	}

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

	/***
	 *  隐藏手机号码，已判断香港手机号码和大陆手机号码
	 * @return
	 */
	public static String hidePhoneNumber(String phone){
		if(TextUtils.isEmpty(phone)) return phone;
		String result = phone;
		if (phone.length() == 8) {// 香港号码
			result = phone.substring(0, 2)+"****"+phone.substring(6, 8);
		} else if(phone.length()>=11){
			result = phone.substring(0,3)+"****"+phone.substring(7,phone.length());
		}
		return result;
	}
}
