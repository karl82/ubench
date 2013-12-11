#!/bin/sh

perf stat -d java -cp out/production/ubench cz.rank.ubench.bp.BranchPrediction1 > bp-1-perf.log 2>&1
perf stat -d java -cp out/production/ubench cz.rank.ubench.bp.BranchPrediction2 > bp-2-perf.log 2>&1
