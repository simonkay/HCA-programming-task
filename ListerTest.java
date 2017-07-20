/**
 * 
 */
package uk.ac.ebi.ena.accessions;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Unit tests for Lister class.
 * 
 * @author simonk
 * 
 */
public class ListerTest {

	private Lister lister;
	private List<String> accessions;
	private List<String> sortedAccessions;

	/**
	 * Expect the list to be sorted by length of the number in the accession.
	 */
	@Test
	public void differentDigitLengthsButSameLetter() {

		accessions = Arrays.asList("A00", "A0", "A00000", "A000", "A0000");
		lister = new Lister(accessions);
		sortedAccessions = lister.getSortedList();

		assertEquals("A0 expected first", "A0", sortedAccessions.get(0));
		assertEquals("A00 expected second", "A00", sortedAccessions.get(1));
		assertEquals("A000 expected third", "A000", sortedAccessions.get(2));
		assertEquals("A0000 expected fourth", "A0000", sortedAccessions.get(3));
		assertEquals("A00000 expected fifth", "A00000", sortedAccessions.get(4));
	}

	/**
	 * Expect the list to be sorted with all 0's first, then 1's, then 2
	 */
	@Test
	public void differentDigitsAndLengthsButSameLetter() {

		accessions = Arrays
				.asList("A00", "A2", "A001", "A00000", "A0001", "A0");
		lister = new Lister(accessions);
		sortedAccessions = lister.getSortedList();

		assertEquals("A0 expected first", "A0", sortedAccessions.get(0));
		assertEquals("A00 expected second", "A00", sortedAccessions.get(1));
		assertEquals("A00000 expected third", "A00000", sortedAccessions.get(2));
		assertEquals("A0001 expected fourth", "A0001", sortedAccessions.get(3));
		assertEquals("A001 expected fifth", "A001", sortedAccessions.get(4));
		assertEquals("A2 expected sixth", "A2", sortedAccessions.get(5));
	}

	/**
	 * Expect the list to be sorted by their letters first and then obeying
	 * above rules on the numbers.
	 */
	@Test
	public void differentDigitsAndLetters() {

		accessions = Arrays.asList("A00000", "SRR211001", "A0001", "ERR100114",
				"DRR2110012", "ABCDEFG1");
		lister = new Lister(accessions);
		sortedAccessions = lister.getSortedList();

		assertEquals("A00000 expected first", "A00000", sortedAccessions.get(0));
		assertEquals("A0001 expected second", "A0001", sortedAccessions.get(1));
		assertEquals("ABCDEFG1 expected third", "ABCDEFG1",
				sortedAccessions.get(2));
		assertEquals("A0001 expected fourth", "DRR2110012",
				sortedAccessions.get(3));
		assertEquals("A001 expected fifth", "ERR100114",
				sortedAccessions.get(4));
		assertEquals("A001 expected sixth", "SRR211001",
				sortedAccessions.get(5));
	}

	/**
	 * Expect the accessions to be sorted in increasing order with all ranges
	 * picked out.
	 */
	@Test
	public void samePrefixesNoPaddingToRanges() {

		accessions = Arrays.asList("AB24", "AB17", "AB23", "AB40", "AB19",
				"AB21", "AB34", "AB39", "AB20");
		lister = new Lister(accessions);
		sortedAccessions = lister.getSortedList();

		assertEquals("AB17 expected first", "AB17", sortedAccessions.get(0));
		assertEquals("AB19-AB21 expected second", "AB19-AB21",
				sortedAccessions.get(1));
		assertEquals("AB23-AB24 expected third", "AB23-AB24",
				sortedAccessions.get(2));
		assertEquals("AB34 expected fourth", "AB34", sortedAccessions.get(3));
		assertEquals("AB39-AB40 expected fifth", "AB39-AB40",
				sortedAccessions.get(4));
	}

	/**
	 * Expect number padding to be preserved and ranges to still be picked out.
	 */
	@Test
	public void consecutiveNumbersWithPaddingToRange() {

		accessions = Arrays.asList("ERR000113", "ERR100114", "ERR000111",
				"ERR000112");
		lister = new Lister(accessions);

		sortedAccessions = lister.getSortedList();

		assertEquals("Range expected first", "ERR000111-ERR000113",
				sortedAccessions.get(0));
		assertEquals("ERR100114 expected second", "ERR100114",
				sortedAccessions.get(1));
	}

	/**
	 * Expect the example input to be sorted as expected.
	 */
	@Test
	public void exampleInput() {

		accessions = Arrays.asList("A00000", "A0001", "ERR000111", "ERR000112",
				"ERR000113", "ERR000115", "ERR000116", "ERR100114",
				"ERR200000001", "ERR200000002", "ERR200000003", "DRR2110012",
				"SRR211001", "ABCDEFG1");
		lister = new Lister(accessions);

		sortedAccessions = lister.getSortedList();

		assertEquals("A00000 expected first", "A00000", sortedAccessions.get(0));
		assertEquals("A0001 expected second", "A0001", sortedAccessions.get(1));
		assertEquals("ABCDEFG1 expected third", "ABCDEFG1",
				sortedAccessions.get(2));
		assertEquals("DRR2110012 expected fourth", "DRR2110012",
				sortedAccessions.get(3));
		assertEquals("ERR000111-ERR000113 expected fifth",
				"ERR000111-ERR000113", sortedAccessions.get(4));

		assertEquals("ERR000115-ERR000116 expected sixth",
				"ERR000115-ERR000116", sortedAccessions.get(5));

		assertEquals("ERR100114 expected seventh", "ERR100114",
				sortedAccessions.get(6));

		assertEquals("ERR200000001-ERR200000003 expected eigth",
				"ERR200000001-ERR200000003", sortedAccessions.get(7));

		assertEquals("SRR211001 expected ninth", "SRR211001",
				sortedAccessions.get(8));
	}
}
