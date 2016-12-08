package com.qvc.shoppingcart.common;

import com.gigaspaces.annotation.pojo.SpaceClass;
import com.gigaspaces.annotation.pojo.SpaceId;
import com.gigaspaces.annotation.pojo.SpaceIndex;
import com.gigaspaces.metadata.index.SpaceIndexType;

/**
 * Created by sudip on 11/22/2016.
 */
@SpaceClass
public class LineItemTracker {

  private String lineItemId;
  private long expirationTimestamp;

  @SpaceId
  public String getLineItemId() {
    return lineItemId;
  }

  public void setLineItemId(String lineItemId) {
    this.lineItemId = lineItemId;
  }

  @SpaceIndex(type = SpaceIndexType.EXTENDED)
  public long getExpirationTimestamp() {
    return expirationTimestamp;
  }

  public void setExpirationTimestamp(long timestamp) {
    expirationTimestamp = timestamp;
  }

  public LineItemTracker(String lineItemId, long expirationTimestamp) {
    this.lineItemId = lineItemId;
    this.expirationTimestamp = expirationTimestamp;
  }

  public LineItemTracker() {
  }
}
