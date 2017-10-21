package com.example.dell.calculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private int[] butNum = { R.id.tet0, R.id.tet1, R.id.tet2, R.id.tet3, R.id.tet4,
            R.id.tet5, R.id.tet6, R.id.tet7, R.id.tet8, R.id.tet9 };  //数组存贮id
    private Button[] buts = new Button [butNum.length];  //建立连接数字键的按钮
    private int[] butCalcu = { R.id.add, R.id.reduce, R.id.plus, R.id.divis, R.id.divisx, R.id.oppo,
            R.id.x2, R.id.point, R.id.sign, R.id.equal, R.id.del, R.id.C, R.id.CE };  //数组存贮运算键
    private Button[] buttake = new Button [butCalcu.length];  //建立连接运算键的按钮
    TextView tvResult = null;
    private String num1 = null, num2 = null, num3 = null;  //前两个为操作数，num3为结果
    private int op1 = -1, op2 = -1 ,op3 = 0;  //op1表示进行运算的种类，op2主要解决（数字）+（+、-、*、/）+（=）+（=）类计算
    boolean isClickEqu = false;  //表示是否执行了（=）
    boolean isPoint = false;  //表示屏幕上显示的数的最后一位是否为小数点
    boolean beginOfNum = true;  //表示接下来输入的数是否为新的数（用于判断直接输入小数点是否直接置为”0.“）
    boolean isError = false;  //表示1/0的错误情况
    boolean isZero = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        for( int i = 0; i < butNum.length; i++ ){  //绑定监听器
            buts[i] = (Button) findViewById(butNum[i]);
            buts[i].setOnClickListener(listener);
        }
        for( int i = 0; i < butCalcu.length; i++){  //绑定监听器
            buttake[i] = (Button) findViewById(butCalcu[i]);
            buttake[i].setOnClickListener(listener);
        }
        tvResult = (TextView) findViewById(R.id.result);
    }
    public void deal_error(){
        num1 = num2 = num3 = null;
        isClickEqu = false;
        beginOfNum = true;
        isPoint = false;
        isError = false;
        tvResult.setText("0");
        isZero = true;
    }
    public boolean hadPoint(){  //判断是否已经有小数点
        String myString = tvResult.getText().toString();
        if(beginOfNum)
            return false;
        else return (myString.contains("."));
    }
    public void deal_ispoint() {  //末尾是小数点，并且没有数字再输入时，把它舍去
        String myString = tvResult.getText().toString();
        myString = myString.substring(0,myString.length()-1);
        tvResult.setText(myString);
        isPoint = false;
    }
    public static String subZeroAndDot(String s){
        if(s.indexOf(".") > 0){
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
    public void format(){  //输出结果出现有多余的0的情况
        String myStr = tvResult.getText().toString();
        tvResult.setText(subZeroAndDot(myStr));
    }
    public void switchEqual(int op1){  //执行+、-、*、/运算
        double result = 0;
        switch (op1) {
            case 1:
                result = Double.parseDouble(num1) + Double.parseDouble(num2);
                break;
            case 2:
                result = Double.parseDouble(num1) - Double.parseDouble(num2);
                break;
            case 3:
                result = Double.parseDouble(num1) * Double.parseDouble(num2);
                break;
            case 4:
                if(Double.parseDouble(num2) == 0) {  // 1 / 0 的情况
                    String errorStr = "Divisor cannot be zero";
                    tvResult.setText(errorStr);
                    isError = true;
                    return;
                }
                else result = Double.parseDouble(num1) / Double.parseDouble(num2);
                break;
            default:
                return;
        }
        num3 = Double.toString(result);
        tvResult.setText(num3);
        format();
    }
    public void equal(int op1){  // (=)运算符
        format();
        if(num1 == null) return;
        if(isPoint) deal_ispoint();
        num2 = tvResult.getText().toString();
        switchEqual(op1);
        isClickEqu = true;
        beginOfNum = true;
    }
    public View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {  //所有点击事件
            switch (view.getId()) {
                case R.id.tet0:
                    if (tvResult.getText().toString().equals("0"))  //若为0，则不能继续在后面添加0
                        return;
                case R.id.tet1:
                case R.id.tet2:
                case R.id.tet3:
                case R.id.tet4:
                case R.id.tet5:
                case R.id.tet6:
                case R.id.tet7:
                case R.id.tet8:
                case R.id.tet9:
                    if(isError) deal_error();  //发生1/0的错误
                    if(tvResult.getText().toString().equals("0"))
                        isZero = true;
                    isPoint = false;
                    Button bt = (Button) view;
                    if(beginOfNum || isZero){  //新的操作数
                        tvResult.setText(bt.getText().toString());
                        isClickEqu = false;
                    }
                    else {  //继续输入
                        tvResult.setText(tvResult.getText() + bt.getText().toString());
                    }
                    beginOfNum = false;
                    op2 = -1;
                    isZero = false;
                    break;
                case R.id.del:  //删除
                    if (isClickEqu) return;  //不能删除结果。
                    if(isError) {
                        deal_error();
                        return;
                    }
                    if(Double.parseDouble(tvResult.getText().toString()) == 0 && isPoint)
                        tvResult.setText("0");
                    String myStr = tvResult.getText().toString();
                    if(myStr.length() == 2 && myStr.contains("-")){
                        tvResult.setText("0");
                        beginOfNum = true;
                    }
                    else if (myStr.length() > 1)  //正常删除
                        tvResult.setText(myStr.substring(0, myStr.length() - 1));
                    else {  //删除最后一位时，归0
                        tvResult.setText("0");
                        beginOfNum = true;
                    }
                    myStr = tvResult.getText().toString();
                    if(myStr.substring(myStr.length()-1).equals("."))
                        isPoint = true;
                    else isPoint = false;
                    if(tvResult.getText().toString().equals("0"))
                        isZero = true;
                    break;
                case R.id.C:  //全部清零
                    tvResult.setText("0");
                    num1 = null;
                    num2 = null;
                    num3 = null;
                    op1 = op2 = -1;
                    isError = false;
                    isClickEqu = false;
                    beginOfNum = true;
                    isPoint = false;
                    isZero = true;
                    break;
                case R.id.CE:  //清空当前操作数
                    if (isError) deal_error() ;
                    tvResult.setText("0");
                    beginOfNum = true;
                    isClickEqu = false;
                    isZero = true;
                    num3 = "0";
                    op3 = 1;
                    break;
                case R.id.add:  // （+）运算符
                    if (isError) return;  // 1/0情况按键无用
                    if (isPoint) deal_ispoint();  // 操作数最后一位为小数点时，舍弃
                    if (beginOfNum && op3 == 0) num1 = null;  //在第一次调用运算符时（即只有一个操作数），num1为null可使之后（=）的调用跳过
                    equal(op1);  //每一次都调用（=），可以使每一步的结果都立即显示在屏幕上
                    num1 = tvResult.getText().toString();
                    op1 = 1;
                    op2 = 1;
                    op3 = 0;
                    beginOfNum = true;
                    break;
                case R.id.reduce:  // （-）运算符 之后同上
                    if (isError) return;
                    if (isPoint) deal_ispoint();
                    if(Double.parseDouble(tvResult.getText().toString()) == 0)
                        tvResult.setText("0");
                    if (beginOfNum && op3 == 0) num1 = null;
                    equal(op1);
                    num1 = tvResult.getText().toString();
                    op1 = 2;
                    op2 = 2;
                    op3 = 0;
                    beginOfNum = true;
                    break;
                case R.id.plus:  //  （*）运算符 之后同上
                    if (isError) return;
                    if (isPoint) deal_ispoint();
                    if(Double.parseDouble(tvResult.getText().toString()) == 0)
                        tvResult.setText("0");
                    if (beginOfNum && op3 == 0) num1 = null;
                    equal(op1);
                    num1 = tvResult.getText().toString();
                    op1 = 3;
                    op2 = 3;
                    op3 = 0;
                    beginOfNum = true;
                    break;
                case R.id.divis:  //  （/）运算符 之后同上
                    if (isError) return;
                    if (isPoint) deal_ispoint();
                    if(Double.parseDouble(tvResult.getText().toString()) == 0)
                        tvResult.setText("0");
                    if (beginOfNum && op3 == 0) num1 = null;
                    equal(op1);
                    num1 = tvResult.getText().toString();
                    op1 = 4;
                    op2 = 4;
                    op3 = 0;
                    beginOfNum = true;
                    break;
                case R.id.oppo:  //取反
                    if(isError) return;  //  1/0错误情况
                    String strOppo = tvResult.getText().toString();
                    if(strOppo.equals("0")) return;
                    if (strOppo.contains("-")) strOppo = strOppo.substring(1);  //  判断当前值的正负
                    else strOppo = "-" + strOppo;
                    tvResult.setText(strOppo);
                    break;
                case R.id.x2:  //平方  之后同上
                    if (isError) return;
                    if (isPoint) deal_ispoint();
                    double x2;
                    String strX2 = tvResult.getText().toString();
                    x2 = Double.parseDouble(strX2);
                    x2 = x2 * x2;
                    tvResult.setText(Double.toString(x2));
                    format();
                    beginOfNum = true;
                    isClickEqu = true;
                    break;
                case R.id.sign:  //根号 之后同上
                    if (isError) return;
                    if (isPoint) deal_ispoint();
                    double sign;
                    String strSign = tvResult.getText().toString();
                    sign = Double.parseDouble(strSign);
                    sign = Math.sqrt(sign);
                    tvResult.setText(Double.toString(sign));
                    format();
                    beginOfNum = true;
                    isClickEqu = true;
                    break;
                case R.id.divisx:  //取倒数
                    if (isError) return;
                    if (isPoint) deal_ispoint();
                    double divisx;
                    String strDivisx = tvResult.getText().toString();
                    divisx = Double.parseDouble(strDivisx);
                    if (divisx == 0) {  //  1/0的情况
                        String Error = "Divisor cannot be zero";
                        tvResult.setText(Error);
                        isError = true;
                        return;
                    }
                    divisx = 1 / divisx;
                    tvResult.setText(Double.toString(divisx));
                    format();
                    beginOfNum = true;
                    isClickEqu = true;
                    break;
                case R.id.point:  //小数点
                    if (isError) return;  // 1/0情况
                    if (hadPoint()) return;  //已经是小数
                    if (beginOfNum) {  //新的操作数的开始
                        tvResult.setText("0.");
                        beginOfNum = false;
                        isClickEqu = false;
                        isPoint =true;
                        isZero = false;
                        return;
                    }
                    String myStrPoint = tvResult.getText().toString();
                    myStrPoint = myStrPoint + ".";
                    tvResult.setText(myStrPoint);
                    isPoint = true;
                    isClickEqu = false;
                    beginOfNum = false;
                    isZero = false;
                    break;
                case R.id.equal:  // 手动按下的（=）
                    if (isError) {
                        deal_error();  // 1/0的情况
                        return;
                    }
                    if(isPoint) deal_ispoint();
                    format();
                    isClickEqu = true;
                    beginOfNum = true;
                    if (op2 == 0 && num3 != null && num2 != null) {  // （数字）+（+、-、*、/）+（=）+（=）+（=）的情况
                        double result = Double.parseDouble(num3);
                        switch (op1) {
                            case 1:
                                result = result + Double.parseDouble(num2);
                                break;
                            case 2:
                                result = result - Double.parseDouble(num2);
                                break;
                            case 3:
                                result = result * Double.parseDouble(num2);
                                break;
                            case 4:
                                result = result / Double.parseDouble(num2);
                                break;
                        }
                        num3 = Double.toString(result);
                        tvResult.setText(num3);
                        format();
                        op2 = 0;
                        isClickEqu = true;
                        beginOfNum = true;
                        num1 = null;
                    } else {  //与上述相对的普通情况
                        equal(op1);
                        num1 = null;
                        op2 = 0;
                    }
                    break;
            }

        }
    };
}
