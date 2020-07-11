import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;

import static com.sun.tools.attach.VirtualMachine.list;

public class DomParse {
    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        //API规范：需要用一个工厂来造解析器对象，于是我先造了一个工厂！
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

        //获取解析器对象
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        //获取到解析XML文档的File对象
        InputStream inputStream = DomParse.class.getClassLoader().getResourceAsStream("city.xml");

        //解析XML文档，得到了代表XML文档的Document对象！
        Document document = documentBuilder.parse(inputStream);

        //把代表XML文档的document对象传递进去给list方法
        list(document);
         read(document);
         add(document);
         add2(document);
        list(document);
        //暂时不知道怎么往硬盘里面修改
    }


    //我们这里就接收Node类型的实例对象吧！多态！！！
    private static void list(Node node) {

        //判断是否是元素节点，如果是元素节点就直接输出
        if (node.getNodeType() == Node.ELEMENT_NODE) {
            System.out.println(node.getNodeName());
        }

        //....如果没有进入if语句，下面的肯定就不是元素节点了，所以获取到子节点集合
        NodeList nodeList = node.getChildNodes();

        //遍历子节点集合
        for (int i = 0; i < nodeList.getLength(); i++) {

            //获取到其中的一个子节点
            Node child = nodeList.item(i);

            //...判断该子节点是否为元素节点，如果是元素节点就输出，不是元素节点就再获取到它的子节点集合...递归了

            list(child);
        }

    }
    private static void read(Document document) {

        //获取到所有名称为guangzhou节点
        NodeList nodeList = document.getElementsByTagName("guangzhou");

        //取出第一个名称为guangzhou的节点
        Node node = nodeList.item(0);

        //获取到节点的文本内容
        String value = node.getTextContent();

        System.out.println(value);

    }
    private static void add(Document document) throws TransformerException {

        //创建需要增加的节点
        Element element = document.createElement("hangzhou");

        //向节点添加文本内容
        element.setTextContent("杭州");

        //得到需要添加节点的父节点
        Node parent = document.getElementsByTagName("china").item(0);

        //把需要增加的节点挂在父节点下面去
        parent.appendChild(element);

        //获取一个转换器它需要工厂来造，那么我就造一个工厂
        TransformerFactory transformerFactory = TransformerFactory.newInstance();

        //获取转换器对象
        Transformer transformer = transformerFactory.newTransformer();

        //把内存中的Dom树更新到硬盘中
        transformer.transform(new DOMSource(document),new StreamResult("city.xml"));

    }
    private  static void add2(Document document) throws TransformerException {

        //获取到beijing节点
        Node beijing = document.getElementsByTagName("beijing").item(0);

        //创建新的节点
        Element element = document.createElement("guangxi");

        //设置节点的文本内容
        element.setTextContent("广西");

        //获取到要创建节点的父节点，
        Node parent = document.getElementsByTagName("china").item(0);

        //将guangxi节点插入到beijing节点之前！
        parent.insertBefore(element, beijing);

        //将内存中的Dom树更新到硬盘文件中
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult("city.xml"));

    }
    private static void delete(Document document) throws TransformerException {

        //获取到beijing这个节点
        Node node = document.getElementsByTagName("beijing").item(0);

        //获取到父节点，然后通过父节点把自己删除了
        node.getParentNode().removeChild(node);

        //把内存中的Dom树更新到硬盘文件中
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(new DOMSource(document), new StreamResult("city.xml"));


    }
}