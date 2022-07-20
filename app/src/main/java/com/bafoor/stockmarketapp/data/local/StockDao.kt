package com.bafoor.stockmarketapp.data.local

import androidx.room.*

@Dao
interface StockDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListing(
        companyListingEntity : List<CompanyListingEntity>
    )

    @Query("DELETE FROM companylistingentity")
    suspend fun clearCompanyListing(
    )

    @Query(
        """
            SELECT *
            FROM companylistingentity
            WHERE LOWER(name) LIKE '%' || LOWER(:query) || '%' OR
            UPPER(:query) == symbol
        """
    )
    suspend fun searchCompanyListing(query : String) : List<CompanyListingEntity>


    /**
     * query of searchCompanyListing if i search about tesla and i write %tEs%
     * ROOM convert query to lowercase and compare it with name which is always in lowercase or
     * see if query with Uppercase = symbol which is always in UPPER
     * % ---> like a sequence of what i write
     */


}



















