import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IPropertyType } from '../property-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../property-type.test-samples';

import { PropertyTypeService } from './property-type.service';

const requireRestSample: IPropertyType = {
  ...sampleWithRequiredData,
};

describe('PropertyType Service', () => {
  let service: PropertyTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: IPropertyType | IPropertyType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(PropertyTypeService);
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

    it('should create a PropertyType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const propertyType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(propertyType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a PropertyType', () => {
      const propertyType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(propertyType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a PropertyType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of PropertyType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a PropertyType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addPropertyTypeToCollectionIfMissing', () => {
      it('should add a PropertyType to an empty array', () => {
        const propertyType: IPropertyType = sampleWithRequiredData;
        expectedResult = service.addPropertyTypeToCollectionIfMissing([], propertyType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(propertyType);
      });

      it('should not add a PropertyType to an array that contains it', () => {
        const propertyType: IPropertyType = sampleWithRequiredData;
        const propertyTypeCollection: IPropertyType[] = [
          {
            ...propertyType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addPropertyTypeToCollectionIfMissing(propertyTypeCollection, propertyType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a PropertyType to an array that doesn't contain it", () => {
        const propertyType: IPropertyType = sampleWithRequiredData;
        const propertyTypeCollection: IPropertyType[] = [sampleWithPartialData];
        expectedResult = service.addPropertyTypeToCollectionIfMissing(propertyTypeCollection, propertyType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(propertyType);
      });

      it('should add only unique PropertyType to an array', () => {
        const propertyTypeArray: IPropertyType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const propertyTypeCollection: IPropertyType[] = [sampleWithRequiredData];
        expectedResult = service.addPropertyTypeToCollectionIfMissing(propertyTypeCollection, ...propertyTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const propertyType: IPropertyType = sampleWithRequiredData;
        const propertyType2: IPropertyType = sampleWithPartialData;
        expectedResult = service.addPropertyTypeToCollectionIfMissing([], propertyType, propertyType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(propertyType);
        expect(expectedResult).toContain(propertyType2);
      });

      it('should accept null and undefined values', () => {
        const propertyType: IPropertyType = sampleWithRequiredData;
        expectedResult = service.addPropertyTypeToCollectionIfMissing([], null, propertyType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(propertyType);
      });

      it('should return initial array if no PropertyType is added', () => {
        const propertyTypeCollection: IPropertyType[] = [sampleWithRequiredData];
        expectedResult = service.addPropertyTypeToCollectionIfMissing(propertyTypeCollection, undefined, null);
        expect(expectedResult).toEqual(propertyTypeCollection);
      });
    });

    describe('comparePropertyType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.comparePropertyType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.comparePropertyType(entity1, entity2);
        const compareResult2 = service.comparePropertyType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.comparePropertyType(entity1, entity2);
        const compareResult2 = service.comparePropertyType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.comparePropertyType(entity1, entity2);
        const compareResult2 = service.comparePropertyType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
