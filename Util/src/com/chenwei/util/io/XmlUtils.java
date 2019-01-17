package com.chenwei.util.io;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;
import java.util.Scanner;

/**
 * @author chenwei
 * @date 2019-01-17 14:49
 **/
public class XmlUtils {

    /**
     * 使用Marshaller将Bean转为XML
     *
     * @param t
     * @param <T>
     * @return
     * @throws JAXBException
     */
    public static <T> String beanToXml(T t) throws JAXBException {
        StringWriter stringWriter = new StringWriter();
        beanToXml(t, stringWriter);
        return stringWriter.getBuffer().toString();
    }

    /**
     * 使用Marshaller将Bean转为XML
     *
     * @param t
     * @param xmlStringWriter
     * @param <T>
     * @throws JAXBException
     */
    public static <T> void beanToXml(T t, StringWriter xmlStringWriter) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(t, xmlStringWriter);
    }

    /**
     * 使用Marshaller将Bean转为XML
     *
     * @param t
     * @param writer
     * @param <T>
     * @throws JAXBException
     */
    public static <T> void beanToXml(T t, Writer writer) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(t, writer);
    }

    /**
     * 使用Marshaller将Bean转为XML
     *
     * @param t
     * @param outputStream
     * @param <T>
     * @throws JAXBException
     */
    public static <T> void beanToXml(T t, OutputStream outputStream) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(t.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.marshal(t, outputStream);
    }

    /**
     * 使用Unmarshaller将XML转为Bean
     *
     * @param t
     * @param xmlStr
     * @param <T>
     * @return
     * @throws JAXBException
     */
    public static <T> T xmlToBean(Class<T> t, String xmlStr) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(t);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        StringReader stringReader = new StringReader(xmlStr);
        T tObject = (T) unmarshaller.unmarshal(stringReader);
        return tObject;
    }

    /**
     * 使用Unmarshaller将XML转为Bean
     *
     * @param t
     * @param xmlStr
     * @param <T>
     * @return
     * @throws JAXBException
     */
    public static <T> T xmlToBean(T t, String xmlStr) throws JAXBException {
        return (T) xmlToBean(t.getClass(), xmlStr);
    }

    /**
     * 使用Unmarshaller将XML转为Bean
     *
     * @param t
     * @param inputStream
     * @param <T>
     * @return
     * @throws JAXBException
     */
    public static <T> T xmlToBean(T t, InputStream inputStream) throws JAXBException {
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return (T) xmlToBean(t.getClass(), stringBuilder.toString());
    }

    /**
     * 使用Unmarshaller将XML转为Bean
     *
     * @param tClass
     * @param inputStream
     * @param <T>
     * @return
     * @throws JAXBException
     */
    public static <T> T xmlToBean(Class<T> tClass, InputStream inputStream) throws JAXBException {
        Scanner scanner = new Scanner(inputStream);
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            stringBuilder.append(scanner.nextLine());
        }
        return (T) xmlToBean(tClass, stringBuilder.toString());
    }

}
