package com.piechart;

import android.graphics.Color;
import android.icu.math.BigDecimal;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener
{
    private Button button1;
    private Button button2;
    private Button button3;
//    private Button button4;
//    private Button button5;
    private double i;
    private MyPieChart pie_chart;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_piechart);
        init();
    }

    private void init()
    {
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
//        button4 = (Button) findViewById(R.id.button4);
//        button5 = (Button) findViewById(R.id.button5);
        button1.setOnClickListener(clickListener);
        button2.setOnClickListener(clickListener);
        button3.setOnClickListener(clickListener);
//        button4.setOnClickListener(clickListener);
//        button5.setOnClickListener(clickListener);
        pie_chart = (MyPieChart) findViewById(R.id.pie_chart);
        pie_chart.setUsePercentValues(true);
        //pie_chart.setDrawEntryLabels(true);
        //pie_chart.setEnabled(false);
        pie_chart.getDescription().setEnabled(false);//Description:描述,Enabled:启用
        //pie_chart.setDrawingCacheEnabled(false);

        pie_chart.setDrawSliceText(true);//Slice:片
        //pie_chart.getDescription();
        pie_chart.setExtraOffsets(0.f, 10.f, 0.f, 10.f);
        pie_chart.setDragDecelerationFrictionCoef(0.95f);
        pie_chart.setCenterText("饼状图");
        pie_chart.setDrawHoleEnabled(true);
        pie_chart.setHoleColor(Color.parseColor("#63B8FF")); //中间圆颜色
        pie_chart.setTransparentCircleColor(Color.WHITE);
        pie_chart.setTransparentCircleAlpha(110);
        pie_chart.setHoleRadius(40f);  //圆半径
        pie_chart.setTransparentCircleRadius(42f);//Transparent:透明,Circle:圈,Radius:半径
        pie_chart.setDrawCenterText(true);
        //pie_chart.setRotationAngle(-90);
        pie_chart.setRotationAngle(0);
        //the chart by touch
        pie_chart.setRotationEnabled(true);
        pie_chart.setHighlightPerTapEnabled(true);
        // 添加一个选择监听器
        pie_chart.setOnChartValueSelectedListener(this);//Selected:挑选出来的

        //setPieData1(pie_chart);

//        for (IDataSet<?> set : pie_chart.getData().getDataSets())
//            set.setDrawValues(!set.isDrawValuesEnabled());
//        pie_chart.invalidate();
//        pie_chart.getLegend().setEnabled(false);//设置比例图
//        pie_chart.animateX(1400);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    //private void setPieData1(MyPieChart chart)
    private void setPieData1(MyPieChart chart,Map<String, Object> formatDatas)
    {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();
        for (Map.Entry<String,Object> entry:formatDatas.entrySet())
        {
            String s = entry.getValue().toString();
            double x = Integer.valueOf(s).intValue();
            i = i + x;
        }
        for (Map.Entry<String,Object> entry:formatDatas.entrySet())
        {
            String s = entry.getValue().toString();
            double y = Integer.valueOf(s).intValue();
            double f =  y/i;
            BigDecimal  b  =  new BigDecimal(f);
            double  f1  =  b.setScale(4,  BigDecimal.ROUND_HALF_UP).doubleValue();
            double result = Math.round(f1 * 100) / 100d; // 12345.68
            entries.add(new PieEntry((float) f1,entry.getKey().toString() + ":" + entry.getValue().toString() + "人" + "," + "占比" + ":" + result + "%"));

        }
        //^^^
        PieDataSet dataSet = new PieDataSet(entries, "Election Results");
        dataSet.setSliceSpace(1f);  //不同块之间的间距
        dataSet.setSelectionShift(7f);//选中时候突出的间距
        //dataSet.setValueLineWidth(10f);
        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.mintcream));
        colors.add(getResources().getColor(R.color.bai));
        colors.add(getResources().getColor(R.color.black_blue));
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(getResources().getColor(R.color.colorPrimary));
        colors.add(getResources().getColor(R.color.orange));
        dataSet.setColors(colors);
        //dataSet.setValueLinePart1OffsetPercentage(100f);//Part:部分,Percentage:百分比
        //dataSet.setValueLineVariableLength(true);//Variable:变量,Length:长度
        dataSet.setValueLinePart1Length(0.6f);
        dataSet.setValueLinePart2Length(0.2f);
        dataSet.setHighlightEnabled(true);
        //dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        dataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);//标签显示在外面，关闭显示在饼图里面
        //dataSet.setValueLineColor(0xff000000);  //设置指示线条颜色,必须设置成这样，才能和饼图区域颜色一致
        dataSet.setValueLineColor(Color.BLACK);
        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());//Formatter:格式化程序,Percent,百分比
        data.setValueTextSize(12f);
        data.setHighlightEnabled(true);
        chart.setData(data);
        chart.highlightValues(null);
        chart.invalidate();
        //^^^
//        for (IDataSet<?> set : chart.getData().getDataSets())
//            set.setDrawValues(!set.isDrawValuesEnabled());
//        chart.invalidate();
        chart.getLegend().setEnabled(false);//设置比例图
        chart.animateX(1400);
        //^^^
    }
    @Override
    public void onValueSelected(Entry entry, Highlight highlight) {
    }
    @Override
    public void onNothingSelected() {
    }
    private View.OnClickListener clickListener = new View.OnClickListener()
    {
        @RequiresApi(api = Build.VERSION_CODES.N)//VERSION:版本，CODES：码
        //@TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.button1:
                    Map<String, Object> formatDatas1 = new HashMap<>();
                    StringBuilder key1=new StringBuilder();
                    key1.setLength(0);
                    key1.append("男人");
                    formatDatas1.put(key1.toString(),"675137");
                    key1.setLength(0);
                    key1.append("女人");
                    formatDatas1.put(key1.toString(),"564781");
                    setPieData1(pie_chart,formatDatas1);
                    break;
                case R.id.button2:
                    Map<String, Object> formatDatas2 = new HashMap<>();
                    StringBuilder key2=new StringBuilder();
                    key2.setLength(0);
                    key2.append("无");
                    formatDatas2.put(key2.toString(),"0");

                    key2.setLength(0);
                    key2.append("学生1");
                    formatDatas2.put(key2.toString(),"82429");

                    key2.setLength(0);
                    key2.append("学生2");
                    formatDatas2.put(key2.toString(),"10201");

                    key2.setLength(0);
                    key2.append("学生2");
                    formatDatas2.put(key2.toString(),"121606");

                    key2.setLength(0);
                    key2.append("学生3");
                    formatDatas2.put(key2.toString(),"159966");

                    key2.setLength(0);
                    key2.append("学生4");
                    formatDatas2.put(key2.toString(),"12892");

                    key2.setLength(0);
                    key2.append("学生5");
                    formatDatas2.put(key2.toString(),"246117");
                    setPieData1(pie_chart,formatDatas2);
                    break;
                case R.id.button3:
                    Map<String, Object> formatDatas3 = new HashMap<>();
                    StringBuilder key3=new StringBuilder();
                    key3.setLength(0);
                    key3.append("无");
                    formatDatas3.put(key3.toString(),"5158");

                    key3.setLength(0);
                    key3.append("学生6");
                    formatDatas3.put(key3.toString(),"393855");

                    key3.setLength(0);
                    key3.append("学生7");
                    formatDatas3.put(key3.toString(),"38062");

                    key3.setLength(0);
                    key3.append("学生8");
                    formatDatas3.put(key3.toString(),"6855");

                    key3.setLength(0);
                    key3.append("学生9");
                    formatDatas3.put(key3.toString(),"54633");

                    key3.setLength(0);
                    key3.append("学生10");
                    formatDatas3.put(key3.toString(),"26413");

                    key3.setLength(0);
                    key3.append("学生11");
                    formatDatas3.put(key3.toString(),"891");

                    key3.setLength(0);
                    key3.append("学生12");
                    formatDatas3.put(key3.toString(),"20422");

                    key3.setLength(0);
                    key3.append("学生13");
                    formatDatas3.put(key3.toString(),"27261");

                    key3.setLength(0);
                    key3.append("学生14");
                    formatDatas3.put(key3.toString(),"43842");

                    key3.setLength(0);
                    key3.append("学生15");
                    formatDatas3.put(key3.toString(),"832");

                    key3.setLength(0);
                    key3.append("学生16");
                    formatDatas3.put(key3.toString(),"14987");

                    setPieData1(pie_chart,formatDatas3);
                    break;
            }
        }
    };
}
