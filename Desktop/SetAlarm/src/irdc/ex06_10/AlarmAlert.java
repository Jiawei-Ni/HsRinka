package irdc.ex06_10; /* import相关class */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle; /* 实际跳出闹铃Dialog的Activity */

public class AlarmAlert extends Activity
{
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState); /* 跳出的闹铃警示 */
    new AlertDialog.Builder(AlarmAlert.this).setIcon(R.drawable.clock)
        .setTitle("闹钟响了!!").setMessage("赶快起床吧!!!").setPositiveButton(
            "关掉他", new DialogInterface.OnClickListener()
            {
              public void onClick(DialogInterface dialog,
                  int whichButton)
              { /* 关闭Activity */
                AlarmAlert.this.finish();
              }
            }).show();
  }
}