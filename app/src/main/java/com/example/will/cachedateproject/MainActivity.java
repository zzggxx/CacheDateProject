package com.example.will.cachedateproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class MainActivity extends AppCompatActivity {

    private String TAG = "MainActivity";
    private Gson mGson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*----------------字节流------------------------------------------------------------------*/
//        getFilePath(this, "test");
//
//        writeFile();
//        readFile();
//
//        readTxt();
//        writeTxt();
//
//        /*-----原始的流读写-----*/
//        copyFile();
//        bufferCopyFile();


        /*----------------字符流------------------------------------------------------------------*/
        /*-----原始的流读写-----*/
//        fileReader();
//        fileWriter();
        /*-----缓冲流读写-----*/
//        fileCopy();

        /*--------数据库的简单操作-----------------------------------------------------------------*/
        ExampleDataBaseOpenHelper dataBaseOpenHelper =
                new ExampleDataBaseOpenHelper(this, "example2.db", null, 1);
        SQLiteDatabase db = dataBaseOpenHelper.getReadableDatabase();
        /*-----1,原始的方式进行增删改查-----*/
//        增
//        db.execSQL("insert into student (name,sex) values (?,?)", new Object[]{"will", "男"});

//        删除
//        db.execSQL("delete from student where name = 'will' ");
//        db.execSQL("delete from student where name = ? ", new Object[]{"周高雄"});

//        改
//        db.execSQL("update student set sex='big man___' where name = '周高雄'");
//        db.execSQL("update student set sex=? where name = ?", new Object[]{"big man", "周高雄"});

//        查
//        Cursor cursor = db.rawQuery("select sex from student where name = ?", new String[]{"周高雄"});
//        String string = null;
//        while (cursor.moveToNext()) {
//            string = cursor.getString(0);
//            Log.i(TAG, "onCreate: __" + string);
//        }
        /*-----2,android的有返回值的方式进行增删改查-----*/
//        增
//        ContentValues values = new ContentValues();
//        values.put("name", "vivian");
//        values.put("sex", "women");
//        for (int i = 0; i < 10; i++) {
//            long student = db.insert("student", null, values);
//        }
//        删
//        int student = db.delete("student", "name = ?", new String[]{"tom"});
//        改
//        values.put("name", "vivian");
//        db.update("student", values, "name = ?", new String[]{"vivian__"});
//        查
//        Cursor cursor = db.query(
//                "student", new String[]{"sex"},
//                "name=?", new String[]{"vivian"},
//                null, null, null);
//        boolean result = cursor.moveToNext();
//        String sex = null;
//        if (result) {
//            sex = cursor.getString(0);
//            Log.i(TAG, "onCreate: " + sex);
//        }

        /*------------------------------json------------------------------------------------------*/
        BufferedReader bufferedReader = null;
        StringBuffer stringBuffer = null;
        try {
            InputStream summaryjson = this.getAssets().open("summaryjson");
            bufferedReader = new BufferedReader(new InputStreamReader(summaryjson));
            String line = null;
            stringBuffer = new StringBuffer();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String toString = stringBuffer.toString();

        /*----------------------------------android系统自带的方法----------------------------------*/

//        1,互换:json-String字符串到JSONObject(string串中拿到字段这种方法可以使用),反过来toString()即可:
        try {
            JSONObject jsonObject = new JSONObject(toString);

            int total_noassign_num = jsonObject.getInt("total_noassign_num");

            JSONObject total_assign_num = jsonObject.getJSONObject("total_assign_num");
            total_assign_num.getString("name");//会抛异常建议使用optString();
            total_assign_num.optString("name");

            JSONArray this_month_summary = jsonObject.getJSONArray("this_month_summary");
            this_month_summary.toString();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*----------------------------------Google的Gson方法----------------------------------*/
//        1,json-String到bean对象.
        SummaryBean summaryBean = mGson.fromJson(toString, SummaryBean.class);
//        2,bean到json-string
        String s = mGson.toJson(summaryBean);


        Gson gson = new Gson();
        Foo<Bar> foo = new Foo<Bar>();
        String s1 = gson.toJson(foo);// May not serialize foo.value correctly
        Foo foo1 = gson.fromJson(s1, foo.getClass());// Fails to deserialize foo.value as Bar

    }

    class Foo<T> {
        T value;
    }

    class Bar {
        String name;
        int age;
    }

    private void fileCopy() {
        String path = getFilesDir() + File.separator + "test.txt";
        String pathCopy = getFilesDir() + File.separator + "testcopy.txt";
        FileReader fileReader = null;
        FileWriter fileWriter = null;
        try {
            fileReader = new FileReader(path);
            fileWriter = new FileWriter(pathCopy);

//            1,一个字节一个字节的读写
            int b = -1;
            while ((b = fileReader.read()) != -1) {
                fileWriter.write(b);
            }

//            2,指定数组大小,因为不知道码表所以没有avaiable.
            char[] arr = new char[1024];
            int c = -1;
            while ((c = fileReader.read(arr)) != -1) {
                fileWriter.write(arr);
            }

//           3,使用自带缓冲流buffer(关闭的时候和字节流一样也是只关闭buffer)
            BufferedReader bufferedReader = new BufferedReader(fileReader);
//            使用指定的码表读文件,写文件也是类似.
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            int ch;
            while ((ch = bufferedReader.read()) != -1) {
                bufferedWriter.write(ch);
            }

//            4,使用特有的readLine
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                bufferedWriter.write(line);
                bufferedWriter.newLine();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void fileWriter() {
        String path = getFilesDir() + File.separator + "test.txt";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path, true);
            fileWriter.write("追加的");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @NonNull
    private void fileReader() {
        String path = getFilesDir() + File.separator + "test.txt";
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(path);
            int b = -1;
            while ((b = fileReader.read()) != -1) {
//                char进行了码表强转.
                System.out.println("___" + (char) b);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void bufferCopyFile() {

        String path = getFilesDir() + File.separator + "wangxiang.mp3";
        String pathCache = getCacheDir() + File.separator + "wangxiang.mp3";
        BufferedInputStream bufferedInputStream = null;
        BufferedOutputStream bufferedOutputStream = null;

        try {

            bufferedInputStream = new BufferedInputStream(new FileInputStream(path));
            bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(pathCache));
            int b = -1;
            while ((b = bufferedInputStream.read()) != -1) {
                bufferedOutputStream.write(b);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedInputStream != null) {
                try {
                    bufferedInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bufferedOutputStream != null) {
                try {
                    bufferedOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void copyFile() {

        FileInputStream fileInputStream = null;
        FileOutputStream outputStream = null;

        String path = getFilesDir() + File.separator + "wangxiang.mp3";
        String pathCache = getCacheDir() + File.separator + "wangxiang.mp3";

        try {
            fileInputStream = new FileInputStream(path);
            outputStream = new FileOutputStream(pathCache);
            int b = -1;

//            1,一个字节一个字节的读
            while ((b = fileInputStream.read()) != -1) {
                outputStream.write(b);
            }

//            2,全部一次性读
            byte[] bytes = new byte[fileInputStream.available()];
            fileInputStream.read(bytes);
            outputStream.write(bytes);

//            3,推荐方法,自己定义一个固定大小的数组进行读写

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /*-------------------------自己写IO------------------------------------------------------*/

    private void writeTxt() {
        FileOutputStream outputStream = null;
        try {
            String path = getFilesDir() + File.separator + "test.txt";
            outputStream = new FileOutputStream(path, true);
            outputStream.write("\n第三行,外部写入22223333".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void readTxt() {
        AssetManager assets = this.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assets.open("test.txt");
            int b = -1;
            while (inputStream.read() != -1) {
                int read = inputStream.read();
                System.out.println("__" + read);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 缓存文件的地址
     *
     * @param context
     * @param dir
     * @return
     */
    public static String getFilePath(Context context, String dir) {
        String directoryPath = "";
        //判断SD卡是否可用
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            //空间剩余量,sd卡的根目录.
            if (Environment.getExternalStorageDirectory().getFreeSpace() / 1024 / 1024 > 2) {
                directoryPath = context.getExternalFilesDir(dir).getAbsolutePath();
                // directoryPath =context.getExternalCacheDir().getAbsolutePath() ;
            }
        } else {
            //没内存卡就存机身内存
            directoryPath = context.getFilesDir() + File.separator + dir;
            // directoryPath=context.getCacheDir()+File.separator+dir;
        }
        File file = new File(directoryPath);
        if (!file.exists()) {//判断文件目录是否存在
            file.mkdirs();
        }
        return directoryPath;
    }

    /*-----------------------------自带API打开路径读写-----------------------------------------------*/

    /**
     * 私有文件夹_file读
     */
    private void readFile() {
        BufferedReader bufferedReader = null;
        StringBuffer content = new StringBuffer();
        try {
            FileInputStream fileInputStream = openFileInput("test.txt");
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line);
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        Toast.makeText(this, content, Toast.LENGTH_LONG).show();
        System.out.println(content);
    }

    /**
     * 私有文件夹_file写
     */
    private void writeFile() {
        BufferedWriter bufferedWriter = null;
        try {
//            打开一个私有目录即/data/data/应用程序包名/files目录下的文件，读入到输入流中,如果文件不存在直接创建
            FileOutputStream outputStream = openFileOutput("test.txt", Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
            bufferedWriter.write("写入到了内部存贮区,/data/data/包名/file/test\n第二行");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedWriter != null) {
                try {
                    bufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
