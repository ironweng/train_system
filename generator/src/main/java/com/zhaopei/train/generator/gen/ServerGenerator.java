package com.zhaopei.train.generator.gen;

import com.zhaopei.train.generator.util.FreemarkerUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ServerGenerator {
    static String serverPath = "[module]/src/main/java/com/zhaopei/train/[module]/service/";
    static String pomPath = "generator/pom.xml";

    static {
        new File(serverPath).mkdirs();
    }

    public static void main(String[] args) throws Exception {
        // 获取mybatis-generator
        String generatorPath = getGeneratorPath();
        // 比如generator-config-member.xml，得到module = member
        String module = generatorPath.replace("src/main/resources/generator-config-", "").replace(".xml", "");
        System.out.println("module: " + module);
        serverPath = serverPath.replace("[module]", module);
        new File(serverPath).mkdirs();
        System.out.println("servicePath: " + serverPath);

        // 读取table节点
        Document document = new SAXReader().read("generator/" + generatorPath);
        Node table = document.selectSingleNode("//table");
        System.out.println(table);
        Node tableName = table.selectSingleNode("@tableName");
        Node domainObjectName = table.selectSingleNode("@domainObjectName");
        System.out.println(tableName.getText() + "/" + domainObjectName.getText());

        // 示例：表名 zhaopei_test
        // Domain = ZhaopeiTest
        String Domain = domainObjectName.getText();
        // domain = zhaopeiTest
        String domain = Domain.substring(0, 1).toLowerCase() + Domain.substring(1);
        // do_main = zhaopei-test
        String do_main = tableName.getText().replaceAll("_", "-");

        //组装参数
        Map<String, Object> param = new HashMap<>();
        param.put("Domain", Domain);
        param.put("domain", domain);
        param.put("do_main", do_main);
        System.out.println("组装参数：" + param);

        FreemarkerUtil.initConfig("service.ftl");
        FreemarkerUtil.generator(serverPath + Domain + "Service.java",param);
    }


    private static String getGeneratorPath() throws DocumentException {
        SAXReader saxReader = new SAXReader();
        Map<String, String> map = new HashMap<String, String>();
        map.put("pom", "http://maven.apache.org/POM/4.0.0");
        saxReader.getDocumentFactory().setXPathNamespaceURIs(map);
        Document document = saxReader.read(pomPath);
        Node node = document.selectSingleNode("//pom:configurationFile");
        System.out.println(node.getText());
        return node.getText();
    }

}
