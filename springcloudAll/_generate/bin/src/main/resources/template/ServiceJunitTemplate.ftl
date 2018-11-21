package com.erongdu.cf.bussiness.test;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.erongdu.cf.domain.Pages;
import ${domMainPath}.${className};
import ${servicePath}.${className}Service;

public class ${className}ServiceTest extends BaseTest{

	@Autowired
	private ${className}Service $!{lowerName}Service;
	

	@Test
	public void testAdd(){
		try {
			${className} $!{lowerName} = new ${className}();
			$!{lowerName}Service.add($!{lowerName});
			id = $!{lowerName}.getId();
			System.out.println("-testAdd-----id---------"+id);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testList(){
		try {
			${className}Page $!{lowerName}Page = new ${className}Page();
			List<${className}> $!{lowerName}s = $!{lowerName}Service.queryByList($!{lowerName}Page);
			for(${className} e:$!{lowerName}s){
				System.out.println(e.getName());
			}
			System.out.println("----testList----------");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Test
	public void testDel(){
		try {
			$!{lowerName}Service.delete(id);
			System.out.println("---testDel-----------");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
