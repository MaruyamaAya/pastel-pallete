package backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PythonScriptsMonitor {
    private final static String SCRIPT_DIR_PATH = new File("scripts").getAbsolutePath();

    static synchronized MessageReceived messageProcess(MessageToSend messageToSend) {
        try {
            Process process = Runtime.getRuntime().exec(
                    new String[]{"python", SCRIPT_DIR_PATH + "\\message_backend.py"});
            InputStream in = process.getInputStream();
            OutputStream out = process.getOutputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out, "gbk"));
            String inputJsonString = messageToSend.toJsonString();
            bw.write(inputJsonString);
            bw.newLine();
            bw.flush();
            bw.close();
            String line;
            List<String> lines = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                lines.add(line);
            }
            br.close();
            process.waitFor();
            return MessageReceived.fromJsonString(lines.get(lines.size() - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return MessageReceived.err();
    }

    public static void main(String[] args) {
        System.out.println(SCRIPT_DIR_PATH);
        MessageReceived messageReceived = messageProcess(new MessageToSend("test1", 0, 1,
                "E:/JavaProjects/Live2DBackend/scripts/test_bot.wav"));
        messageReceived.print();
    }
}
