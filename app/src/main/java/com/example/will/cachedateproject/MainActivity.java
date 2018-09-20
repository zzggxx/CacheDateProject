package com.example.will.cachedateproject;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*----------------字节流----------------------------*/
//        getFilePath(this, "test");
//
//        writeFile();
//        readFile();
//
//        readTxt();
//        writeTxt();

//        copyFile();
//
//        bufferCopyFile();


        /*----------------字符流----------------------------*/

        String path = getFilesDir() + File.separator + "test.txt";
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(path,true);
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
                System.out.println("__" + (char) b);
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
