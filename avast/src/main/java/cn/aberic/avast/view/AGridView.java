package cn.aberic.avast.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * ScrollView嵌套GridView
 * 
 * @author aberic
 *
 */
public class AGridView extends GridView {

	public AGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AGridView(Context context) {
		super(context);
	}

	public AGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
