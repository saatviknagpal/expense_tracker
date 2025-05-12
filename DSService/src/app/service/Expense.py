from pydantic import BaseModel, Field
from typing import Optional

class Expense(BaseModel):
    amount: Optional[str] = Field(title="expense_amount", description="The amount of the expense")
    merchant: Optional[str] = Field(title="expense_merchant", description="The merchant of the expense")
    currency: Optional[str] = Field(title="expense_currency", description="The currency of the expense")

    
