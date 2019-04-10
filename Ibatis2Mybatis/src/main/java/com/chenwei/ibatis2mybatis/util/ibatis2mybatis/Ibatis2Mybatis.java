package com.chenwei.ibatis2mybatis.util.ibatis2mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.*;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author chenwei
 * @date 2018/11/8 7:25 PM
 **/
@Slf4j
public class Ibatis2Mybatis {
    private static String sourcePath;
    private static String targetPath;
    private static String IS_NOT_NULL_TEMPLATE_STRING = "{propertyName} != null";
    private static String IS_NOT_EMPTY_TEMPLATE_STRING = "{propertyName} != null and {propertyName} != ''";
    private static String IS_EQUAL_TEMPLATE_STRING = "{property1}!= null and {property1} == '{property2}'.toString()";

    public static Map<String, List<File>> getFiles(String baseFilePath) {
        Map<String, List<File>> fileMap = new LinkedHashMap<>();
        File file = new File(baseFilePath);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            Arrays.asList(files).stream().forEach(
                    tmpFile -> {
                        if (!tmpFile.exists()) {
                        } else if (tmpFile.isDirectory()) {
                            fileMap.putAll(getFiles(tmpFile.getAbsolutePath()));
                        } else if (tmpFile.getName().contains("xml")) {
                            if (fileMap.containsKey(tmpFile.getParent())) {
                                fileMap.get(tmpFile.getParent()).add(tmpFile);
                            } else {
                                List<File> fileList = new LinkedList<>();
                                fileList.add(tmpFile);
                                fileMap.put(tmpFile.getParent(), fileList);
                            }
                        } else {
                        }
                    }
            );
        }
        return fileMap;
    }

    public static void render(String path) {
        Map<String, List<File>> files = Ibatis2Mybatis.getFiles(path);
        SAXReader saxReader = new SAXReader();
        Set<String> keySet = files.keySet();
        keySet.stream().forEach(key -> {
            List<File> fileList = files.get(key);
            fileList.stream().forEach(
                    file -> {
                        Document document = null;
                        try {
                            document = saxReader.read(file);
                            replace(document, file);
                            //Element rootElement = document.getRootElement();
                            // processFile2Xml(key, file.getName(), rootElement);
                        } catch (DocumentException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (SAXException e) {
                            e.printStackTrace();
                        }
                    }
            );
        });
    }

    public static void main(String[] args) throws DocumentException {
        Properties properties = System.getProperties();
        sourcePath = properties.getProperty("source.path");
        targetPath = properties.getProperty("target.path");
        if (StringUtils.isBlank(sourcePath)) {
            /*log.error("未指定源文件路径");
            return;*/
            sourcePath = "/Users/cw/Documents/code/cw/ChenWei/Ibatis2Mybatis/src/main/resources/source";
        }
        if (StringUtils.isBlank(targetPath)) {
            /*log.error("未指定目标文件路径");
            return;*/
            targetPath = "/Users/cw/Documents/code/cw/ChenWei/Ibatis2Mybatis/src/main/resources/mapper";
        }
        render(sourcePath);
    }

    public static void replace(Document document, File file) throws IOException, SAXException, DocumentException {
        String xmlStr = document.asXML();
        xmlStr = xmlStr.replace("sqlMap", "mapper")
                .replace("-//ibatis.apache.org//DTD SQL Map 2.0//EN", "-//mybatis.org//DTD Mapper 3.0//EN")
                .replace("http://ibatis.apache.org/dtd/sql-map-2.dtd", "http://mybatis.org/dtd/mybatis-3-mapper.dtd")
                .replaceAll("parameterClass", "parameterType")
                .replaceAll("resultClass", "resultType")
                .replace("$orderByClause$", "${orderByClause}")
                .replaceAll("LONGVARCHAR", "VARCHAR");
        //将类似于#compayId:VARCHAR#的动态字段转换为：#{compayId:VARCHAR}
        xmlStr = processDynamicStr(xmlStr);
        Document parseText = DocumentHelper.parseText(xmlStr);
        //将<isNotNull prepend=","></isNotNull>标签转换为<if test="">** ,</if>
        List<Element> isNotNullWithPrependAnd = parseText.selectNodes("//isNotNull[@prepend=',']");
        processIsNotNullWithPrepend1(isNotNullWithPrependAnd);
        //将<isNotNull prepend="and"></isNotNull>标签转换为<if test="">and **</if>
        List<Element> isNotNullWithPrepend1 = parseText.selectNodes("//isNotNull[@prepend='and']");
        processIsNotNullWithPrependAnd(isNotNullWithPrepend1);
        //将<isNotNull prepend="and"></isNotNull>标签转换为<if test="">**</if>
        List<Element> isNotNullWithOutPrepend = parseText.selectNodes("//isNotNull");
        processIsNotNullWithOutPrepend(isNotNullWithOutPrepend);

        //将<isNotEmpty prepend=","></isNotEmpty>标签转换为<if test="">** ,</if>
        List<Element> isNotEmptyWithPrependAnd = parseText.selectNodes("//isNotEmpty[@prepend=',']");
        processIsNotEmptyWithPrepend1(isNotEmptyWithPrependAnd);
        //将<isNotEmpty prepend="and"></isNotEmpty>标签转换为<if test="">and **</if>
        List<Element> isNotEmptyWithPrepend1 = parseText.selectNodes("//isNotEmpty[@prepend='and']");
        processIsNotEmptyWithPrependAnd(isNotEmptyWithPrepend1);
        //将<isNotEmpty prepend="and"></isNotEmpty>标签转换为<if test="">**</if>
        List<Element> isNotEmptyWithOutPrepend = parseText.selectNodes("//isNotEmpty");
        processIsNotEmptyWithOutPrepend(isNotEmptyWithOutPrepend);

        //将<isEqual prepend=","></isEqual>标签转换为<if test="">** ,</if>
        List<Element> isEqualWithPrepend1 = parseText.selectNodes("//isEqual[@prepend=',']");
        processIsEqualWithPrepend1(isEqualWithPrepend1);
        //将<isEqual prepend="and"></isEqual>标签转换为<if test="">and **</if>
        List<Element> isEqualWithPrependAnd = parseText.selectNodes("//isEqual[@prepend='and']");
        processIsEqualWithPrependAnd(isEqualWithPrependAnd);
        //将<isEqual prepend="and"></isEqual>标签转换为<if test="">**</if>
        List<Element> isEqualWithOutPrepend = parseText.selectNodes("//isEqual");
        processIsEqualWithOutPrepend(isEqualWithOutPrepend);

        //将<isParameterPresent prepend="and"></isParameterPresent>标签转换为<if test="">**</if>
        List<Element> isParameterPresentNodes = parseText.selectNodes("//isParameterPresent");
        processIsParameterPresent(isParameterPresentNodes);
        //将<dynamic prepend="and"></dynamic>标签转换为<trim/where>**</trim/where>

        List<Element> dynamicNodes = parseText.selectNodes("//dynamic");
        processDynamicNodes(dynamicNodes);

        //将<iterate></iterate>转换为<foreach></foreach>
        List<Element> iterate = parseText.selectNodes("//iterate");
        processIterate(iterate);

        List insertNodes = parseText.selectNodes("//insert");
        processInsertNodes(insertNodes);
        List resultMaps = parseText.selectNodes("//resultMap");
        processResultMaps(resultMaps);
        String path = file.getPath().replace(sourcePath, "");
        write(targetPath + path, parseText);
        log.info("");
    }

    private static void processIterate(List<Element> iterate) {
        iterate.forEach(
                element -> {

                }
        );
    }

    private static void processDynamicNodes(List<Element> dynamicNodes) {
        dynamicNodes.forEach(
                element -> {
                    List list = element.selectNodes("./@prepend");
                    element.setName("trim");
                    Attribute prepend = element.attribute("prepend");
                    element.remove(prepend);
                    if (prepend != null) {
                        element.addAttribute("prefix", prepend.getValue());
                    }
                }
        );
    }

    private static void processIsParameterPresent(List<Element> isParameterPresentNodes) {
        isParameterPresentNodes.forEach(
                element -> {
                    element.setName("if");
                    element.addAttribute("test", "_parameter != null");
                }
        );
    }

    private static void processIsEqualWithOutPrepend(List<Element> isEqualWithOutPrepend) {
        isEqualWithOutPrepend.forEach(
                element -> {
                    processEqualWithOutPrepend(element);
                }
        );
    }

    private static void processIsEqualWithPrependAnd(List<Element> isEqualWithPrependAnd) {
        isEqualWithPrependAnd.forEach(
                element -> {
                    processEqualWithOutPrepend(element);
                    String text = element.getText();
                    element.setText(" and " + text);
                }
        );
    }

    private static void processIsEqualWithPrepend1(List<Element> isEqualWithPrepend1) {
        isEqualWithPrepend1.forEach(
                element -> {
                    processEqualWithOutPrepend(element);
                    String text = element.getText();
                    element.setText(text + " , ");
                }
        );

    }

    private static void processEqualWithOutPrepend(Element element) {
        Attribute property = element.attribute("property");
        Attribute compareValue = element.attribute("compareValue");
        element.remove(property);
        element.remove(compareValue);
        element.setName("if");
        element.addAttribute("test", IS_EQUAL_TEMPLATE_STRING.replace("{property1}", property.getValue()).replace("{property2}", compareValue.getValue()));
    }

    private static void processIsNotEmptyWithOutPrepend(List<Element> isNotEmptyWithOutPrepend) {
        isNotEmptyWithOutPrepend.forEach(
                element -> {
                    processPrepend(element, 1);
                }
        );

    }

    private static void processIsNotEmptyWithPrepend1(List<Element> isNotEmptyWithPrepend1) {
        isNotEmptyWithPrepend1.forEach(
                element -> {
                    processPrepend(element, 1);
                    String text = element.getText();
                    element.setText(text + " , ");
                }
        );
    }

    private static void processIsNotEmptyWithPrependAnd(List<Element> isNotEmptyWithPrependAnd) {
        isNotEmptyWithPrependAnd.forEach(
                element -> {
                    processPrepend(element, 1);
                    String text = element.getText();
                    element.setText(" and " + text);
                }
        );
    }

    private static void processIsNotNullWithOutPrepend(List<Element> isNotNullWithOutPrepend) {
        isNotNullWithOutPrepend.forEach(
                element -> {
                    processPrepend(element, 0);
                }
        );

    }

    private static void processIsNotNullWithPrepend1(List<Element> isNotNullWithPrepend1) {
        isNotNullWithPrepend1.forEach(
                element -> {
                    processPrepend(element, 0);
                    String text = element.getText();
                    element.setText(text + " , ");
                }
        );
    }

    private static void processIsNotNullWithPrependAnd(List<Element> isNotNullWithPrependAnd) {
        isNotNullWithPrependAnd.forEach(
                element -> {
                    processPrepend(element, 0);
                    String text = element.getText();
                    element.setText(" and " + text);
                }
        );
    }

    private static void processPrepend(Element element, int type) {
        Attribute property = element.attribute("property");
        Attribute prepend = element.attribute("prepend");
        if (prepend != null) {
            element.remove(prepend);
        }
        if (property != null) {
            element.remove(property);
            String value = property.getValue();
            if (type == 0) {
                element.addAttribute("test", IS_NOT_NULL_TEMPLATE_STRING.replace("{propertyName}", value));
            } else if (type == 1) {
                element.addAttribute("test", IS_NOT_EMPTY_TEMPLATE_STRING.replace("{propertyName}", value));
            }
        }
        element.setName("if");
    }

    private static String processDynamicStr(String xmlStr) {
        String regex = "#[^#]*#";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(xmlStr);
        while (m.find()) {
            String group = m.group();
            String ss = group.replaceFirst("#", "<tmp>").replace("#", "}").replace("<tmp>", "#{");
            xmlStr = xmlStr.replace(group, ss);
        }
        return xmlStr;
    }

    private static void processInsertNodes(List<Element> insertNodes) {
        insertNodes.forEach(
                element -> {

                }
        );
    }

    public static void write(String path, Document document) throws IOException {
        File file = new File(path.substring(0, path.lastIndexOf("/")));
        if (!file.exists()) {
            file.mkdirs();
        }
        OutputFormat format = OutputFormat.createPrettyPrint();
        format.setEncoding("UTF-8");
        XMLWriter writer = new XMLWriter(new FileWriter(path), format);
        writer.write(document);
        writer.flush();
        writer.close();
    }

    private static void processResultMaps(List<Element> elementList) {
        elementList.forEach(
                element -> {
                    Attribute nameAttr = element.attribute("class");
                    String attrValue = nameAttr.getValue();
                    element.remove(nameAttr);
                    element.addAttribute("type", attrValue);
                    //处理jdbcType为LONGVARCHAR的result节点
                    List<Element> longVarcharNodes = element.selectNodes("//result[@jdbcType='LONGVARCHAR']");
                    longVarcharNodes.forEach(
                            childElement -> {
                                childElement.remove(childElement.attribute("jdbcType"));
                                childElement.addAttribute("jdbcType", "VARCHAR");
                            }
                    );
                    //处理关联查询节点
                    List<Element> selectNodes = element.selectNodes("//result[@select]");
                    selectNodes.forEach(
                            selectNode -> {
                                selectNode.setName("association");
                            }
                    );
                }
        );
    }


}
