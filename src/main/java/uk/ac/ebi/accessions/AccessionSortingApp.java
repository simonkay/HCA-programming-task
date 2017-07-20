/**
 * 
 */
package uk.ac.ebi.ena.accessions;

import java.util.LinkedList;
import java.util.List;

/**
 * Command line accession sorting application.
 * 
 * @author simonk
 * 
 */
public class AccessionSortingApp {

	/**
	 * Expects the list of accessions to be provided as command line arguments.
	 * Deals with comma separated lists as input. Writes the output as a
	 * comma-separated list.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		List<String> inputList = new LinkedList<String>();

		// Add all the arguments stripping out any commas
		for (String arg : args) {
			inputList.add(arg.replace(",", ""));
		}

		Lister lister = new Lister(inputList);

		List<String> processedList = lister.getSortedList();

		String output = processedList.toString().replace("[", "")
				.replace("]", "");

		System.out.println(output);
	}
}
