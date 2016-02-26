package cn.aberic.avast.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * ScrollView嵌套ListView
 * 
 * @author aberic
 *
 */
public class AListView extends ListView {

	public AListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AListView(Context context) {
		super(context);
	}

	public AListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
