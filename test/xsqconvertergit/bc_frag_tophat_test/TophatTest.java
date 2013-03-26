/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package xsqconvertergit.bc_frag_tophat_test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import xsqconvertergit.XSQConverterGit;

/**
 *
 * @author root
 */
public class TophatTest {
    
    private static final File tmpDir = new File(System.getProperty("java.io.tmpdir"));
    
    private static File testBaseOutputDir;     
    private static File testXSQOutputDir;    
    private static File testConversionMetricsFile;    
    
    private static List<String> tags;
    private static List<String> libraries;        
        
    public TophatTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
        Assert.assertTrue("Unable to create " + tmpDir.getAbsolutePath(), tmpDir.exists() || tmpDir.mkdirs());
        
        String XSQbaseName = "HU03_20121219_MaartjeChip2R_Nico_minimalTest_L02";
        
        testBaseOutputDir = new File(tmpDir, XSQbaseName + "_fastq");        
        testXSQOutputDir = new File(testBaseOutputDir, XSQbaseName);        
        testConversionMetricsFile = new File(testXSQOutputDir, "conversionMetrics.txt");
        
        tags = Arrays.asList(new String[]{"F3"}); 
        libraries = Arrays.asList(new String[]{"10B32DJ","5SIDJ","5WM1DJ" });           
        
        String testXSQFilePath = TophatTest.class.getResource("HU03_20121219_MaartjeChip2R_Nico_minimalTest_L02.xsq").getFile();
          
        List<String> arguments = new ArrayList<String>();
        arguments.add("-i");
        arguments.add(testXSQFilePath);
        arguments.add("-o");
        arguments.add(testBaseOutputDir.getPath());
        arguments.add("-f");
        arguments.add("CSFASTA");
          
        XSQConverterGit.main(arguments.toArray(new String[0]));
        
    }
    
    @AfterClass
    public static void tearDownClass() {
        try {
            //remove the base output dir and all its content
            FileUtils.deleteDirectory(testBaseOutputDir);
        } catch (IOException ex) {
            Logger.getLogger(TophatTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Before
    public void setUp() {    }
    
    @After
    public void tearDown() {   }

       
    @Test
    public void testBaseOutputDir() {
        
        Assert.assertTrue( "Base output dir " + testBaseOutputDir.getAbsolutePath() + "does not exist", testBaseOutputDir.exists());
    }
    
    @Test
    public void testXSQOutputDir() {
        Assert.assertTrue( "XSQ output dir " + testXSQOutputDir.getAbsolutePath() + "does not exist", testXSQOutputDir.exists());
    }
    
    @Test
    public void testLibraryTagOutputDir() {
        
        for(String library : libraries)
        {
            for(String tag: tags)
            {
                File libraryAndtagDir = new File(testXSQOutputDir, library+"_"+tag);
                Assert.assertTrue( "Library and tag output dir " + libraryAndtagDir.getAbsolutePath() + "does not exist", libraryAndtagDir.exists());
            }
        }       
    }
    
    @Test
    public void testLibraryTagReadsOutputDir() {
        
        for(String library : libraries)
        {
            for(String tag: tags)
            {
                File libraryAndtagDir = new File(testXSQOutputDir, library+"_"+tag);
                File libraryAndtagAndReadsDir = new File(libraryAndtagDir, "reads");                
                Assert.assertTrue( "Library and tag reads output dir " + libraryAndtagAndReadsDir.getAbsolutePath() + "does not exist", libraryAndtagAndReadsDir.exists());
            }
        } 
    }
    
    @Test
    public void testConversionMetricsFile() {
        Assert.assertTrue( "Conversion metric file " + testConversionMetricsFile.getAbsolutePath() + "does not exist", testConversionMetricsFile.exists());
        
        File expectedConversionMetricsFile  = new File(getClass().getResource("conversionMetrics.txt").getFile());        
       
        junitx.framework.FileAssert.assertEquals("Conversion metrics files is not like expected", testConversionMetricsFile, expectedConversionMetricsFile);                 
        
    }    
    
    @Test
    public void testFastQFiles() {
        
        for(String library : libraries)
        {
            for(String tag: tags)
            {
                File libraryAndtagDir = new File(testXSQOutputDir, library+"_"+tag);
                File libraryAndtagAndReadsDir = new File(libraryAndtagDir, "reads");
                
                File testFastaFile = new File(libraryAndtagAndReadsDir, "p1."+library+"_"+tag+".csfasta");
                Assert.assertTrue( "FastQ file " + testFastaFile.getAbsolutePath() + "does not exist", testFastaFile.exists());
                File expectedFastQFile  = new File(getClass().getResource(testFastaFile.getName()).getFile()); 
                 
                
                junitx.framework.FileAssert.assertEquals("Fastq file "+testFastaFile.getPath()+" is not like expected", testFastaFile, expectedFastQFile);  
                
                File testQualFile = new File(libraryAndtagAndReadsDir, "p1."+library+"_"+tag+".qual");
                Assert.assertTrue( "FastQ file " + testQualFile.getAbsolutePath() + "does not exist", testQualFile.exists());
                File expectedQualFile  = new File(getClass().getResource(testQualFile.getName()).getFile()); 
                 
                
                junitx.framework.FileAssert.assertEquals("Fastq file "+testQualFile.getPath()+" is not like expected", testQualFile, expectedQualFile);  
            }
        }         
    }     
    
}