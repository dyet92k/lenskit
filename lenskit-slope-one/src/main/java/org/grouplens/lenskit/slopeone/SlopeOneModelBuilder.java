package org.grouplens.lenskit.slopeone;

import it.unimi.dsi.fastutil.longs.LongIterator;

import org.grouplens.lenskit.RecommenderComponentBuilder;
import org.grouplens.lenskit.baseline.BaselinePredictor;
import org.grouplens.lenskit.data.Ratings;
import org.grouplens.lenskit.data.vector.SparseVector;

/**
 * Pre-computes the deviations and number of mutual rating users for every pair
 *  of items and stores the results in a <tt>DeviationMatrix</tt> and 
 *  <tt>CoratingMatrix</tt>. These matrices are later used by a 
 *  <tt>SlopeOneRatingPredictor</tt>.
 */
public class SlopeOneModelBuilder extends RecommenderComponentBuilder<SlopeOneModel> {

	private CoratingMatrix commonUsers;
	private DeviationMatrix deviations;
	private BaselinePredictor baseline;
	private DeviationComputer devComp;

	/**
	 * Constructs a <tt>SlopeOneModel</tt> and the necessary matrices from
	 * a <tt>RatingSnapshot</tt>.
	 */
	@Override
	public SlopeOneModel build() {
		commonUsers = new CoratingMatrix(snapshot);
		deviations = new DeviationMatrix(snapshot);
		for (long currentUser : snapshot.getUserIds()) {
			SparseVector ratings = Ratings.userRatingVector(snapshot.getUserRatings(currentUser));
			LongIterator iter = ratings.keySet().iterator();
			while (iter.hasNext()) {
				long item1 = iter.next();
				LongIterator iter2 = ratings.keySet().tailSet(item1).iterator();
				if (iter2.hasNext()) iter2.next();
				while (iter2.hasNext()) {
					long item2 = iter2.next();
					commonUsers.put(item1, item2, commonUsers.get(item1, item2)+1);
					if (Double.isNaN(deviations.get(item1, item2)))
							deviations.put(item1, item2, ratings.get(item1)-ratings.get(item2));
					else
						deviations.put(item1, item2, deviations.get(item1, item2)+ratings.get(item1)-ratings.get(item2));
				}
			}
		}
		deviations.compute(devComp, commonUsers);
		return new SlopeOneModel(commonUsers, deviations, baseline);
	}

	/**
	 * @param The <tt>BaselinePredictor</tt> to be included in the constructed <tt>SlopeOneModel</tt>
	 */
	public void setBaselinePredictor(BaselinePredictor predictor) {
		baseline = predictor;
	}

	/**
	 * @param comp The <tt>DeviationComputer</tt> object used to fill
	 * the </tt>DeviationMatrix</tt> constructed by the builder.
	 */
	public void setDeviationComputer(DeviationComputer comp) {
		devComp = comp;
	}
}