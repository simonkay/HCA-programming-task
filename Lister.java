/**
 * 
 */
package uk.ac.ebi.ena.accessions;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Class for sorting a list of accession, compacting any consecutive groups of
 * accessions into ranges using "start-end" syntax.
 * 
 * @author simonk
 * 
 */
public class Lister {

	private final List<String> accessions;
	private List<String> sortedList;
	private final static String ACCESSION_SPLIT_REGEX = "(?<=\\D)(?=\\d)";

	/**
	 * Constructor taking a list of accessions.
	 * 
	 * @param accessions
	 */
	public Lister(List<String> accessions) {

		this.accessions = accessions;
		this.sortedList = new LinkedList<String>();
	}

	/**
	 * Sorts the input accessions and then processes ranges, returning the
	 * processed list.
	 * 
	 * @return
	 */
	public List<String> getSortedList() {

		Collections.sort(accessions);
		convertToRanges();

		return sortedList;
	}

	/**
	 * Iterates through a list of accessions, adding any items or ranges when
	 * two non-consecutive accessions are found.
	 */
	private void convertToRanges() {

		String sequenceBegin = accessions.get(0);
		String previous = accessions.get(0);

		for (int index = 1; index < accessions.size(); index++) {

			String current = accessions.get(index);

			if (!isConsecutive(previous, current)) {

				// Add sequence up to previous item and reset beginning
				// of sequence to current item.
				addRangeOrItemToList(sequenceBegin, previous);
				sequenceBegin = current;
			}

			previous = current;
		}

		// Process the final item from input which was set to previous above.
		addRangeOrItemToList(sequenceBegin, previous);
	}

	/**
	 * Checks whether two parameters are the same, in which case, this is
	 * considered a single item to be added. Otherwise it represents a range
	 * from beginning to current accession.
	 * 
	 * @param sequenceBegin
	 * @param accession
	 */
	private void addRangeOrItemToList(String sequenceBegin, String accession) {

		if (accession.equals(sequenceBegin)) {
			sortedList.add(accession);
		} else {
			sortedList.add(sequenceBegin + "-" + accession);
		}
	}

	/**
	 * Applies various rules to determine whether the previous and current
	 * accession are to be considered consecutive.
	 * 
	 * @param previous
	 * @param current
	 * @return
	 */
	private boolean isConsecutive(String previous, String current) {

		String[] previousParts = previous.split(ACCESSION_SPLIT_REGEX);
		String[] currentParts = current.split(ACCESSION_SPLIT_REGEX);

		try {
			if (!currentParts[0].equals(previousParts[0])) {
				// Letter part of accessions are not the same.
				return false;
			} else if (currentParts[1].length() != previousParts[1].length()) {
				// Padded length of digit part not the same.
				return false;
			} else {
				// Check whether consecutive integers.
				return ((Integer.parseInt(currentParts[1])
						- Integer.parseInt(previousParts[1]) == 1));
			}
		} catch (NumberFormatException e) {
			// If the second part of the accession cannot be converted to an
			// integer, assume this is not a range. Should be logged as an
			// error.
			return false;
		} catch (IndexOutOfBoundsException e) {
			// If the accession does not have an integer part,
			// assume this is not a range. Should be logged as an error.
			return false;
		}
	}
}
