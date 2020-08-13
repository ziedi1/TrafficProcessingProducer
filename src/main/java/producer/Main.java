/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * @author ziedi
 */
public class Main {
    
    private final static String TOPIC = "traffic";
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        ProcessBuilder processBuilder = new ProcessBuilder();

        // -- Linux --

        // Run a shell command
        String path = System.getProperty("user.dir");
        processBuilder.command("bash", "-c", "ra -S localhost:561 -n -F "+path+"/src/main/recources/ra.conf");

        // Run a shell script
        //processBuilder.command("path/to/hello.sh");


        try {

            Process process = processBuilder.start();

            StringBuilder output = new StringBuilder();

            BufferedReader reader = new BufferedReader(
			new InputStreamReader(process.getInputStream()));
            //+++++++++++++++++Start Kafka+++++++++++++++++++++
            
            final Producer<String, String> producer = SendTrafficToKafka.createProducer();
           
            ProducerRecord<String, String> record;
            String line;
            System.out.println("while");
            while ((line = reader.readLine()) != null) {
		//output.append(line + "\n");
                System.out.println(line);
                
                record =new ProducerRecord<String, String>(TOPIC, line);
                producer.send(record);
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
		System.out.println("Success!");
		System.out.println(output);
		System.exit(0);
            } else {
		//abnormal...
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
}
    }
    
}
