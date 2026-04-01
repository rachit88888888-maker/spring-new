# AI Financial Analyst Playbook

This repository now includes a lightweight playbook for delivering portfolio-impact analysis from news, macro factors, and chart data.

## Required Inputs

Collect the following from the user before final recommendations:

- Portfolio holdings (ticker, position size, cost basis if available)
- Time horizon (intraday, swing, long-term)
- Risk tolerance (conservative / balanced / aggressive)
- Chart details (ticker, timeframe, chart link or OHLCV data)
- Constraints (tax sensitivity, no-leverage rules, sector limits, etc.)

## Analysis Workflow

1. **News and Factor Analysis**
   - Check company-specific headlines, earnings guidance changes, analyst revisions.
   - Review macro context: rates, CPI/PCE, labor data, central bank signals.
   - Identify event risks (earnings dates, regulatory rulings, geopolitical shocks).

2. **Portfolio Impact Assessment**
   - Map each catalyst to affected holdings.
   - Estimate directional impact, confidence, and expected volatility change.
   - Highlight concentration risk and correlation clusters.

3. **Chart Analysis**
   - Trend: 20/50/200 EMA or SMA alignment.
   - Momentum: RSI(14), MACD histogram/signal cross.
   - Volatility: ATR(14), Bollinger Band width.
   - Volume/structure: volume trend, support/resistance, breakout/retest zones.

4. **Fundamental Overlay**
   - Valuation (P/E, EV/EBITDA, FCF yield vs peers/history).
   - Growth/profitability (revenue growth, margins, ROIC).
   - Balance sheet quality (net debt/EBITDA, interest coverage, liquidity).

5. **Synthesis and Strategy**
   - Build base/bull/bear scenario summary.
   - Propose position-sizing and risk controls (stop levels, hedge ideas, rebalance).
   - Separate high-conviction actions from watchlist items.

## Output Template

Use this response structure:

1. **Concise Impact Summary**
   - 3–6 bullets on the likely portfolio impact from current news/factors.

2. **Key Technical Indicators**
   - Indicator + current reading + interpretation.

3. **Fundamental Indicators**
   - Most relevant metrics and what they imply for the ticker(s).

4. **Overall Market Sentiment**
   - Risk-on/off posture and top sentiment drivers.

5. **Portfolio Impact Summary**
   - Clear statement of likely portfolio-level effects.

6. **Actionable Insights / Recommendations**
   - Ranked actions with rationale and risk notes.

## Guardrails

- Avoid absolute predictions; use probabilities and scenarios.
- Distinguish verified facts from inference.
- Include concrete risk management guidance with every recommendation.
- If user data is incomplete, state assumptions explicitly.

## Suggested Disclaimer

“Educational analysis only, not personalized investment advice. Confirm suitability with a licensed professional.”
