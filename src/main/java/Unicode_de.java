import burp.api.montoya.BurpExtension;
import burp.api.montoya.MontoyaApi;
import burp.api.montoya.ui.contextmenu.ContextMenuEvent;
import burp.api.montoya.ui.contextmenu.ContextMenuItemsProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unicode_de implements BurpExtension {
    private MontoyaApi api;

    @Override
    public void initialize(MontoyaApi api) {
        this.api = api;
        api.extension().setName("unicode_de");
        api.userInterface().registerContextMenuItemsProvider(new MyContextMenus());
        api.logging().logToOutput("# unicode_de 加载成功 #");
        api.logging().logToOutput("# wirte by yyy4nl #");
    }

    private class MyContextMenus implements ContextMenuItemsProvider {
        @Override
        public List<Component> provideMenuItems(ContextMenuEvent event) {
            ArrayList<Component> menus = new ArrayList<>();
            JTextArea textArea = new JTextArea("输入数据");
            JButton decodeButton = new JButton("解码");
            JButton encodeButton = new JButton("编码");
            decodeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String data = textArea.getText();
                    String decodeData = unicode_decode(data);

                    JFrame resultFrame = new JFrame("解码结果");
                    JTextArea resultTextArea = new JTextArea(decodeData);
                    resultTextArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(resultTextArea);

                    resultFrame.getContentPane().add(scrollPane);
                    resultFrame.pack();
                    resultFrame.setVisible(true);
                }
            });
            encodeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String data = textArea.getText();
                    String encodeData = unicode_encode(data);

                    JFrame resultFrame = new JFrame("编码结果");
                    JTextArea resultTextArea = new JTextArea(encodeData);
                    resultTextArea.setEditable(false);
                    JScrollPane scrollPane = new JScrollPane(resultTextArea);

                    resultFrame.getContentPane().add(scrollPane);
                    resultFrame.pack();
                    resultFrame.setVisible(true);
                }
            });

            menus.add(textArea);
            menus.add(decodeButton);
            menus.add(encodeButton);
            return menus;
        }

        private String unicode_decode(String data) {
            Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
            Matcher matcher = pattern.matcher(data);
            StringBuffer de_data = new StringBuffer();
            while (matcher.find()) {
                char ch = (char) Integer.parseInt(matcher.group(2), 16);
                matcher.appendReplacement(de_data, Character.toString(ch));
            }
            matcher.appendTail(de_data);
            return de_data.toString();
        }

        private String unicode_encode(String data) {
            StringBuffer en_data = new StringBuffer();
            for (int i = 0; i < data.length(); i++) {
                // 取出每一个字符
                char c = data.charAt(i);
                // 转换为unicode
                en_data.append("\\u" + Integer.toHexString(c));
            }
            return en_data.toString();
        }
    }
}

