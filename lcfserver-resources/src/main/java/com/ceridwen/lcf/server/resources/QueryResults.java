/* 
 * Copyright 2019 Ceridwen Limited.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ceridwen.lcf.server.resources;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Ceridwen Limited
 * @param <E>
 */
public class QueryResults<E> {
	private int totalResults;
	private int skippedResults;
	private List<E> results;
	
    /**
     *
     * @return
     */
    public int getTotalResults() {
		return totalResults;
	}

    /**
     *
     * @param totalResults
     */
    public void setTotalResults(int totalResults) {
		this.totalResults = totalResults;
	}

    /**
     *
     * @return
     */
    public int getSkippedResults() {
		return skippedResults;
	}

    /**
     *
     * @param skippedResults
     */
    public void setSkippedResults(int skippedResults) {
		this.skippedResults = skippedResults;
	}

    /**
     *
     * @return
     */
    public List<E> getResults() {
            if (results == null) {
                this.results = new ArrayList();
            }
            return results;
	}

    /**
     *
     * @param results
     */
    public void setResults(List<E> results) {
		this.results = results;
	}
	
}
