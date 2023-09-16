package wertexpro.fvds.svaroggraphs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import wertexpro.fvds.svaroggraphs.ui.tools.ToolsFragment;

public class Main2Activity extends AppCompatActivity {
    private LineChartView plcv;
    public static SharedPreferences sp;
    public static Sender3 sslSender = new Sender3("ncp@scryp.ru", "nGqjjRPtC4WtzZ3h", GlobalValues.file_choose);
    public Activity act;
    public static String email;
    public static EditText userInput;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_main2);
            Toolbar toolbar = findViewById(R.id.toolbar2);
            setSupportActionBar(toolbar);
            act = this;
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GlobalValues.startDestination = "slideshow";
                   Intent i = new Intent(Main2Activity.this, MainActivity.class);
                   startActivityForResult(i, 3);
                   act.overridePendingTransition(R.anim.right2, R.anim.left2);
                   finish();
                }
            });
            sp = PreferenceManager.getDefaultSharedPreferences(this);
            plcv = findViewById(R.id.chart_activity);
            GlobalValues.save = false;
            /*final List<Float>floatList = new ArrayList<>();
            LinearLayout linearLayout = findViewById(R.id.main2);
        linearLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                floatList.add(x);
                if (floatList.get(0) <= 50 && (floatList.get(floatList.size() - 1) - floatList.get(0) >= 200) && event.getAction() == MotionEvent.ACTION_MOVE) {
                    GlobalValues.startDestination = "slideshow";
                    Intent i = new Intent(Main2Activity.this, MainActivity.class);
                    startActivityForResult(i, 3);
                    act.overridePendingTransition(R.anim.right2, R.anim.left2);
                    finish();
                }
                if (event.getAction() == MotionEvent.ACTION_UP)floatList.clear();
                return true;
            }
        });*/

        List<String> axisData = new ArrayList<>(16);
            List<Float> yAxisData = new ArrayList<>(16);
            /*ObjectAnimator objectAnimator = ObjectAnimator.ofObject(R.id.back_button, "backgroundColor",
                    new ArgbEvaluator(),
                    ContextCompat.getColor(this, R.color.colorFon),
                    ContextCompat.getColor(this, R.color.colorPrimaryDark));

// 2
            objectAnimator.setRepeatCount(10);
            objectAnimator.setRepeatMode(ValueAnimator.REVERSE);

// 3
            objectAnimator.setDuration(DEFAULT_ANIMATION_DURATION);
            objectAnimator.start();*/
            try {
                ArrayList<String> arr = new ArrayList<>(16);
                ArrayList<String> arr1 = new ArrayList<>(16);
                ArrayList<String> arr2 = new ArrayList<>(16);

                Scanner scanner = new Scanner(new File(android.os.Environment.getExternalStorageDirectory().toString() + "/" + "number" + GlobalValues.file_choose));
                while (scanner.hasNext()) {
                    arr.add(scanner.nextLine());
                }
                setTitle(arr.get(0));
                String[] str = new String[2];
                for (int i = 1; i < arr.size(); i++) {
                    str = arr.get(i).split("-");
                    arr1.add(str[1]);
                    arr2.add(str[0]);
                }
                for (int i = 0; i < arr1.size(); i++) {
                    axisData.add(arr1.get(i));
                }
                for (int i = 0; i < arr2.size(); i++) {
                    yAxisData.add(Float.parseFloat(arr2.get(i)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            /*Button button = findViewById(R.id.back_button);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Main2Activity.this, MainActivity.class);

                    startActivityForResult(intent, 3);
                    overridePendingTransition(R.anim.left, R.anim.right);
                }
            });*/
            Button button2 = findViewById(R.id.file_send_button);
        button2.getBackground().setColorFilter(Color.parseColor("#105CF0"), PorterDuff.Mode.MULTIPLY);
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*Intent intent = new Intent(act, SendFileActivity.class);
                    act.startActivityForResult(intent, 3);
                    act.overridePendingTransition(R.anim.left, R.anim.right);
*/

                    LayoutInflater li = LayoutInflater.from(act);
                    View promptsView = li.inflate(R.layout.send_email, null);

                    //Создаем AlertDialog
                    AlertDialog.Builder mDialogBuilder = new AlertDialog.Builder(act);

                    //Настраиваем prompt.xml для нашего AlertDialog:
                    mDialogBuilder.setView(promptsView);

                    //Настраиваем отображение поля для ввода текста в открытом диалоге:
                    userInput = (EditText) promptsView.findViewById(R.id.input_text);
                    userInput.setText(sp.getString(ToolsFragment.KEY_EMAIL, ""));

                    //Настраиваем сообщение в диалоговом окне:
                    mDialogBuilder
                            .setCancelable(false)
                            .setPositiveButton("Отправить",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                                Runnable run = new FileThread();
                                                Thread thread = new Thread(run);
                                                thread.start();
                                        }
                                    })
                            .setNegativeButton("Отмена",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,int id) {
                                            dialog.cancel();
                                        }
                                    });

                    //Создаем AlertDialog:
                    AlertDialog alertDialog = mDialogBuilder.create();

                    //и отображаем его:
                    alertDialog.show();

                }
            });

            //String[] axisData = {"0.0", "0.1", "0.2", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8",
            //        "0.9", "1.0", "1.1", "1.2", "1.3", "1.4", "1.5", "1.6", "1.7", "1.8", "1.9", "2.0"};
            //float[] yAxisData = {0.12f, 0.19f, 0.26f, 0.02f, 0.43f, 0.51f, 0.64f, 0.57f, 0.87f, 0.74f, 0.97f, 1.01f,
            //       1.07f, 0.76f, 0.93f, 0.65f, 0.81f, 0.75f, 0.97f, 1.05f, 1.12f};
            List yAxisValues = new ArrayList();
            List axisValues = new ArrayList();
        boolean graph;
        if (sp.getString(ToolsFragment.KEY_GRAPH_OPTIONS, "Кривой график").equals("Ломаный график"))graph = true;
        else graph = false;
            Line line = new Line(yAxisValues).setColor(Color.parseColor("#5400AF"))
                    .setCubic(!graph)
                    .setFilled(sp.getBoolean(ToolsFragment.KEY_FILLED, false));
            for (int i = 0; i < axisData.size(); i++) {
                axisValues.add(i, new AxisValue(i).setLabel(axisData.get(i) + "     "));
            }

            for (int i = 0; i < yAxisData.size(); i++) {
                if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать при нажатии") ||
                        sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать всегда"))
                    yAxisValues.add(new PointValue(i, yAxisData.get(i)).setLabel(axisData.get(i) + " - " + yAxisData.get(i) + "В"));
                else yAxisValues.add(new PointValue(i, yAxisData.get(i)));
            }
            if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Не показывать"))
                line.setHasLabels(false).setHasLabelsOnlyForSelected(false);
            else if (sp.getString(ToolsFragment.KEY_POINTS_DATA, "").equals("Показывать при нажатии"))
                line.setHasLabelsOnlyForSelected(true);
            else line.setHasLabelsOnlyForSelected(false).setHasLabels(true);
            line.setPointColor(Color.parseColor("#0B8AEB"));
            line.setHasPoints(sp.getBoolean(ToolsFragment.KEY_POINTS, false));
            List lines = new ArrayList();
            if (sp.getString(ToolsFragment.KEY_POINTS_FORMAT, "").equals("Ромбообразный"))
                lines.add(line.setShape(ValueShape.DIAMOND));
            else if (sp.getString(ToolsFragment.KEY_POINTS_FORMAT, "").equals("Квадратный"))
                lines.add(line.setShape(ValueShape.SQUARE));
            else lines.add(line.setShape(ValueShape.CIRCLE));
            LineChartData data = new LineChartData();
            data.setLines(lines);
            Axis axis = new Axis();
            axis.setValues(axisValues);
            axis.setTextSize(12);
            axis.setTextColor(Color.parseColor("#0C07E9"));
            axis.setName("x, метр");
            Viewport viewport = new Viewport(plcv.getMaximumViewport());
          /*   viewport.top =110;
             viewport.right = 20;*/
            plcv.setMaximumViewport(viewport);
            plcv.setCurrentViewport(viewport);
            data.setAxisXBottom(axis.setHasLines(sp.getBoolean(ToolsFragment.KEY_LINES, false)));
            Axis yAxis = new Axis();
            data.setAxisYLeft(yAxis.setMaxLabelChars(4).setHasLines(sp.getBoolean(ToolsFragment.KEY_LINES, false)));
            yAxis.setTextColor(Color.parseColor("#0C07E9"));
            yAxis.setTextSize(12);
            yAxis.setName("U, вольт");
            plcv.setLineChartData(data);
        }
    @Override
    public void onBackPressed() {
        GlobalValues.startDestination = "slideshow";
        Intent i = new Intent(Main2Activity.this, MainActivity.class);
        startActivityForResult(i, 3);
        act.overridePendingTransition(R.anim.right2, R.anim.left2);
        finish();
    }

class FileThread implements Runnable{
    @Override
    public void run() {
        try {
            Thread.sleep(500);
            email = userInput.getText().toString().trim();
            if (GlobalValues.sdk_api >= 19){
                sslSender = new Sender3("ncp@scryp.ru", "nGqjjRPtC4WtzZ3h", GlobalValues.file_choose);
                Main2Activity.sslSender.send("NCP-данные", "",
                        "ncp@scryp.ru", Main2Activity.email);
                Main2Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(act, "Данные отправлены", Toast.LENGTH_LONG).show();
                    }
                });

            }else{
                Main2Activity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(act, "К сожалению, ваше устройство не поддерживает данную функцию", Toast.LENGTH_LONG).show();
                    }
                });

            }
        }catch (Exception e){}

    }

}

}