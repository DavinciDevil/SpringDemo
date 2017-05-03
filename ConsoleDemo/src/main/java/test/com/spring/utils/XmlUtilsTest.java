package test.com.spring.utils;

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 

/** 
* XmlUtil Tester.
* 
* @author <Authors name> 
* @since <pre>���� 8, 2017</pre> 
* @version 1.0 
*/ 
public class XmlUtilsTest { 

@Before
public void before() throws Exception { 
} 

@After
public void after() throws Exception { 
} 

/** 
* 
* Method: toNormalObject(String xmlContent, Class<T> clazz) 
* 
*/ 
@Test
public void testToNormalObjectForXmlContentClazz() throws Exception { 
//TODO: Test goes here...
    System.out.println("testToNormalObjectForXmlContentClazz");
} 

/** 
* 
* Method: toNormalObject(byte[] bytes, Class<T> clazz) 
* 
*/ 
@Test
public void testToNormalObjectForBytesClazz() throws Exception { 
//TODO: Test goes here...
    System.out.println("testToNormalObjectForBytesClazz");
} 

/** 
* 
* Method: toNormalXml(Object object) 
* 
*/ 
@Test
public void testToNormalXml() throws Exception { 
//TODO: Test goes here... 
} 


/** 
* 
* Method: xmlToObject(byte[] bytes, Class<T> clazz) 
* 
*/ 
@Test
public void testXmlToObjectForBytesClazz() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XmlUtil.getClass().getMethod("xmlToObject", byte[].class, Class<T>.class);
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: xmlToObject(String xmlContent, Class<T> clazz) 
* 
*/ 
@Test
public void testXmlToObjectForXmlContentClazz() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XmlUtil.getClass().getMethod("xmlToObject", String.class, Class<T>.class);
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

/** 
* 
* Method: objectToXml(Include include, T object) 
* 
*/ 
@Test
public void testObjectToXml() throws Exception { 
//TODO: Test goes here... 
/* 
try { 
   Method method = XmlUtil.getClass().getMethod("objectToXml", Include.class, T.class);
   method.setAccessible(true); 
   method.invoke(<Object>, <Parameters>); 
} catch(NoSuchMethodException e) { 
} catch(IllegalAccessException e) { 
} catch(InvocationTargetException e) { 
} 
*/ 
} 

} 
