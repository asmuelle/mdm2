import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';

import { NamespaceService } from '../service/namespace.service';

import { NamespaceComponent } from './namespace.component';

describe('Namespace Management Component', () => {
  let comp: NamespaceComponent;
  let fixture: ComponentFixture<NamespaceComponent>;
  let service: NamespaceService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [
        RouterTestingModule.withRoutes([{ path: 'namespace', component: NamespaceComponent }]),
        HttpClientTestingModule,
        NamespaceComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            data: of({
              defaultSort: 'id,asc',
            }),
            queryParamMap: of(
              jest.requireActual('@angular/router').convertToParamMap({
                page: '1',
                size: '1',
                sort: 'id,desc',
              })
            ),
            snapshot: { queryParams: {} },
          },
        },
      ],
    })
      .overrideTemplate(NamespaceComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(NamespaceComponent);
    comp = fixture.componentInstance;
    service = TestBed.inject(NamespaceService);

    const headers = new HttpHeaders();
    jest.spyOn(service, 'query').mockReturnValue(
      of(
        new HttpResponse({
          body: [{ id: 123 }],
          headers,
        })
      )
    );
  });

  it('Should call load all on init', () => {
    // WHEN
    comp.ngOnInit();

    // THEN
    expect(service.query).toHaveBeenCalled();
    expect(comp.namespaces?.[0]).toEqual(expect.objectContaining({ id: 123 }));
  });

  describe('trackId', () => {
    it('Should forward to namespaceService', () => {
      const entity = { id: 123 };
      jest.spyOn(service, 'getNamespaceIdentifier');
      const id = comp.trackId(0, entity);
      expect(service.getNamespaceIdentifier).toHaveBeenCalledWith(entity);
      expect(id).toBe(entity.id);
    });
  });
});
