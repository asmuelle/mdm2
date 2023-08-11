import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IWasteTrackingParameters } from '../waste-tracking-parameters.model';
import {
  sampleWithRequiredData,
  sampleWithNewData,
  sampleWithPartialData,
  sampleWithFullData,
} from '../waste-tracking-parameters.test-samples';

import { WasteTrackingParametersService } from './waste-tracking-parameters.service';

const requireRestSample: IWasteTrackingParameters = {
  ...sampleWithRequiredData,
};

describe('WasteTrackingParameters Service', () => {
  let service: WasteTrackingParametersService;
  let httpMock: HttpTestingController;
  let expectedResult: IWasteTrackingParameters | IWasteTrackingParameters[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(WasteTrackingParametersService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a WasteTrackingParameters', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const wasteTrackingParameters = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(wasteTrackingParameters).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a WasteTrackingParameters', () => {
      const wasteTrackingParameters = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(wasteTrackingParameters).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a WasteTrackingParameters', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of WasteTrackingParameters', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a WasteTrackingParameters', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addWasteTrackingParametersToCollectionIfMissing', () => {
      it('should add a WasteTrackingParameters to an empty array', () => {
        const wasteTrackingParameters: IWasteTrackingParameters = sampleWithRequiredData;
        expectedResult = service.addWasteTrackingParametersToCollectionIfMissing([], wasteTrackingParameters);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wasteTrackingParameters);
      });

      it('should not add a WasteTrackingParameters to an array that contains it', () => {
        const wasteTrackingParameters: IWasteTrackingParameters = sampleWithRequiredData;
        const wasteTrackingParametersCollection: IWasteTrackingParameters[] = [
          {
            ...wasteTrackingParameters,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addWasteTrackingParametersToCollectionIfMissing(
          wasteTrackingParametersCollection,
          wasteTrackingParameters
        );
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a WasteTrackingParameters to an array that doesn't contain it", () => {
        const wasteTrackingParameters: IWasteTrackingParameters = sampleWithRequiredData;
        const wasteTrackingParametersCollection: IWasteTrackingParameters[] = [sampleWithPartialData];
        expectedResult = service.addWasteTrackingParametersToCollectionIfMissing(
          wasteTrackingParametersCollection,
          wasteTrackingParameters
        );
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wasteTrackingParameters);
      });

      it('should add only unique WasteTrackingParameters to an array', () => {
        const wasteTrackingParametersArray: IWasteTrackingParameters[] = [
          sampleWithRequiredData,
          sampleWithPartialData,
          sampleWithFullData,
        ];
        const wasteTrackingParametersCollection: IWasteTrackingParameters[] = [sampleWithRequiredData];
        expectedResult = service.addWasteTrackingParametersToCollectionIfMissing(
          wasteTrackingParametersCollection,
          ...wasteTrackingParametersArray
        );
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const wasteTrackingParameters: IWasteTrackingParameters = sampleWithRequiredData;
        const wasteTrackingParameters2: IWasteTrackingParameters = sampleWithPartialData;
        expectedResult = service.addWasteTrackingParametersToCollectionIfMissing([], wasteTrackingParameters, wasteTrackingParameters2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(wasteTrackingParameters);
        expect(expectedResult).toContain(wasteTrackingParameters2);
      });

      it('should accept null and undefined values', () => {
        const wasteTrackingParameters: IWasteTrackingParameters = sampleWithRequiredData;
        expectedResult = service.addWasteTrackingParametersToCollectionIfMissing([], null, wasteTrackingParameters, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(wasteTrackingParameters);
      });

      it('should return initial array if no WasteTrackingParameters is added', () => {
        const wasteTrackingParametersCollection: IWasteTrackingParameters[] = [sampleWithRequiredData];
        expectedResult = service.addWasteTrackingParametersToCollectionIfMissing(wasteTrackingParametersCollection, undefined, null);
        expect(expectedResult).toEqual(wasteTrackingParametersCollection);
      });
    });

    describe('compareWasteTrackingParameters', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareWasteTrackingParameters(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareWasteTrackingParameters(entity1, entity2);
        const compareResult2 = service.compareWasteTrackingParameters(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareWasteTrackingParameters(entity1, entity2);
        const compareResult2 = service.compareWasteTrackingParameters(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareWasteTrackingParameters(entity1, entity2);
        const compareResult2 = service.compareWasteTrackingParameters(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
